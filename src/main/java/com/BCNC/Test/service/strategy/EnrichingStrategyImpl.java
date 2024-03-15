package com.BCNC.Test.service.strategy;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.exception.AlbumNotFoundException;
import com.BCNC.Test.exception.EnrichingAlbumsException;
import com.BCNC.Test.mapper.AlbumMapper;
import com.BCNC.Test.model.AlbumDTO;
import com.BCNC.Test.service.PhotoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EnrichingStrategyImpl implements EnrichingStrategy {
    @Autowired
    private PhotoServiceImpl photoServiceImpl;
    @Autowired
    AlbumMapper albumMapper;
    @Autowired
    private RestTemplate restTemplate;
    private final String ALBUMS_URL = "https://jsonplaceholder.typicode.com/albums";

    @Override
    public List<Album> enrich() {
        List<Album> albumsEnriched;
        try {
            albumsEnriched = loadAlbums();
        } catch (Exception e) {
            throw new EnrichingAlbumsException("Error cargando albums de repositorio", e);
        }

        List<Photo> photos;
        try {
            photos = photoServiceImpl.loadPhotos();
        } catch (Exception e) {
            throw new EnrichingAlbumsException("Error cargando photos de repositorio", e);
        }

        if (!albumsEnriched.isEmpty() && !photos.isEmpty()) {
            albumsEnriched.forEach(album -> {
                List<Photo> photosWithAlbumId = photos.stream()
                        .filter(photo -> Objects.equals(photo.getAlbumId(), album.getId()))
                        .collect(Collectors.toList());
                album.setPhotos(photosWithAlbumId);
            });
        }

        return albumsEnriched;
    }

    public List<Album> loadAlbums() {
        AlbumDTO[] albumArray;
        try {
            albumArray = restTemplate.getForObject(ALBUMS_URL, AlbumDTO[].class);
        } catch (RestClientException e) {
            throw new AlbumNotFoundException("Error cargando albums de repositorio", e);
        }

        if (albumArray == null) {
            throw new AlbumNotFoundException("La API devolvió datos nulos");
        }

        return albumMapper.mapToAlbums(Arrays.asList(albumArray));
    }
}