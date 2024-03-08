package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.exception.*;
import com.BCNC.Test.mapper.AlbumMapper;
import com.BCNC.Test.mapper.PhotoMapper;
import com.BCNC.Test.model.AlbumDTO;
import com.BCNC.Test.model.PhotoDTO;
import com.BCNC.Test.repository.AlbumRepository;

import com.BCNC.Test.service.strategy.EnrichingStrategy;
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
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EnrichingStrategy enrichingStrategy;



    public ResponseEntity<String> enrichAndSaveAlbums(){
        try {
            List<Album> enrichedAlbums = enriching();
            if (!enrichedAlbums.isEmpty()) {
                albumRepository.saveAll(enrichedAlbums);
                return ResponseEntity.status(HttpStatus.OK).body("Almacenado en BDD");
            }
        } catch (Exception e) {
            throw new EnrichAndSaveAlbumsException("Error enriqueciendo y guardando álbumes", e);
        }
        throw new EnrichAndSaveAlbumsException("Error recuperando datos", null);
    }
    @Override
    public ResponseEntity<String> getAlbumsFromDB() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Album> albums = albumRepository.findAll();

            //Convertimos en JSON para poder responder en String y aclarar el posible error de procesamiento del catch
            String jsonResponse = objectMapper.writeValueAsString(albums);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new GetAlbumsFromDBException("Error al obtener álbumes de la base de datos", e);
        }
    }
    @Override
    public ResponseEntity<String> enrichAlbums() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonResponse = objectMapper.writeValueAsString(enriching());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                throw new EnrichAlbumsException("Error al convertir a JSON", e);
            }

        } catch (Exception e) {
            throw new EnrichAlbumsException("Error recuperando datos", e);
        }
    }

    public List<Album> enriching() {
        return enrichingStrategy.enrich();
    }

    public List<Album> loadAlbums() {
        AlbumDTO[] albumArray = null;
        try {
            albumArray = restTemplate.getForObject(ALBUMS_URL, AlbumDTO[].class);
        } catch (RestClientException e) {
            logger.error("Error cargando albums de repositorio", e);
            throw new AlbumNotFoundException("Error cargando albums de repositorio", e);
        }

        if (albumArray == null) {
            throw new AlbumNotFoundException("La API devolvió datos nulos");
        }

        return albumMapper.mapToAlbums(Arrays.asList(albumArray));
    }
}
