package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Service
public class AlbumServiceImpl implements AlbumService{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String albumUrl = "https://jsonplaceholder.typicode.com/albums";
    @Autowired
    private PhotoServiceImpl photoServiceImpl;
    @Override
    public ArrayList<Album> getAllAlbums() {

        ResponseEntity<Album[]> response = restTemplate.getForEntity(albumUrl, Album[].class);

        // Verificamos si el cuerpo de la respuesta es nulo
        if (response.getBody() != null) {
            return new ArrayList<>(Arrays.asList(response.getBody()));
        } else {
            return null;
        }
    }

    public void enrichAndSaveAlbums(){

    }
    public ArrayList<Album> enriching() {
        ArrayList<Album> albums = null;
        ArrayList<Photo> photos = null;
        try {
            albums = getAllAlbums();
            photos = photoServiceImpl.getAllPhotos();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        for (Album album : albums) {
            List<Photo> photosWithAlbumId = photos.stream()
                    .filter(photo -> photo.getAlbum().getId().equals(album.getId()))
                    .toList();
//            album.setPhotos(photos.stream()
//                    .filter(photo -> {
//                        Album photoAlbum = photo.getAlbum();
//                        return photoAlbum != null && album.getId().equals(photoAlbum.getId());
//                    })
//                    .collect(Collectors.toList()));
        }
        return albums;
    }

}
