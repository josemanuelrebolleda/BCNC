package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.exception.EnrichAlbumsException;
import com.BCNC.Test.exception.EnrichAndSaveAlbumsException;
import com.BCNC.Test.exception.GetAlbumsFromDBException;
import com.BCNC.Test.mapper.AlbumMapper;
import com.BCNC.Test.mapper.PhotoMapper;
import com.BCNC.Test.repository.AlbumRepository;
import com.BCNC.Test.service.strategy.EnrichingStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


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

            if (albums.isEmpty()) {
                return new ResponseEntity<>("No hay álbumes en la base de datos", HttpStatus.NOT_FOUND);
            }

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
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonResponse = objectMapper.writeValueAsString(enriching());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                throw new EnrichAlbumsException("Error al convertir a JSON", e);
            }
    }

    public List<Album> enriching() {
        return enrichingStrategy.enrich();
    }
}
