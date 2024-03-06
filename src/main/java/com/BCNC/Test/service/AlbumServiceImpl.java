package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.mapper.AlbumMapper;
import com.BCNC.Test.mapper.PhotoMapper;
import com.BCNC.Test.model.AlbumDTO;
import com.BCNC.Test.model.PhotoDTO;
import com.BCNC.Test.repository.AlbumRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Data
@Service
public class AlbumServiceImpl implements AlbumService{
    private static final Logger logger = LoggerFactory.getLogger(AlbumServiceImpl.class);
    private final String ALBUMS_URL = "https://jsonplaceholder.typicode.com/albums";
    @Autowired
    private PhotoServiceImpl photoServiceImpl;
    @Autowired
    AlbumMapper albumMapper;
    @Autowired
    PhotoMapper photoMapper;
    @Autowired
    AlbumRepository albumRepository;

    public ResponseEntity<String> enrichAndSaveAlbums(){
        try {
            List<Album> enrichedAlbums = enriching();
            albumRepository.saveAll(enrichedAlbums);
            return ResponseEntity.status(HttpStatus.OK).body("Almacenado en BDD");
        } catch (Exception e) {
            logger.error("Error recuperando datos", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error recuperando datos");
        }
    }

    @Override
    public ResponseEntity<List<Album>> getAlbumsFromDB() {
        //JMRD Devolver String?
        try {
            List<Album> albums = albumRepository.findAll();
            return new ResponseEntity<>(albums, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<String> enrichAlbums() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //Convertimos en JSON para poder responder en String y aclarar el posible error de procesamiento del catch
                String jsonResponse = objectMapper.writeValueAsString(enriching());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                logger.error("Error al convertir a JSON", e);
                return new ResponseEntity<>("Error al convertir a JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error recuperando datos");
        }

    }


    public List<Album> enriching() {
        List<Album> albumsEnriched;
        try {
            albumsEnriched = loadAlbums();
        } catch (Exception e) {
            logger.error("Error cargando albums de repositorio", e);
            throw new RuntimeException(e);
        }

        albumsEnriched.forEach(album -> {
            List<Photo> photos;
            try {
                photos = photoServiceImpl.loadPhotos();
            } catch (Exception e) {
                logger.error("Error cargando photos de repositorio", e);
                throw new RuntimeException(e);
            }
            List<Photo> photosWithAlbumId = photos.stream()
                    .filter(photo -> Objects.equals(photo.getAlbumId(), album.getId()))
                    .collect(Collectors.toList());
            album.setPhotos(photosWithAlbumId);
        });

        return albumsEnriched;
    }    public List<Album> loadAlbums() {
        RestTemplate restTemplate = new RestTemplate();
        AlbumDTO[] albumArray = null;
        try {
            albumArray = restTemplate.getForObject(ALBUMS_URL, AlbumDTO[].class);
        } catch (RestClientException e) {
            logger.error("Error cargando albums de repositorio", e);
            throw new RuntimeException("Error cargando albums de repositorio", e);
        }

        if (albumArray == null) {
            throw new RuntimeException("La API devolvió datos nulos");
        }

        List<AlbumDTO> albums = Arrays.asList(albumArray);
        return mapToAlbums(albums);
    }

    private List<Album> mapToAlbums(List<AlbumDTO> albumDTOs) {
        return albumMapper.mapToAlbums(albumDTOs);
    }
}
