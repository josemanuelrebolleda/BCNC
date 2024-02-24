package com.BCNC.Test.controller;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.repository.AlbumRepository;
import com.BCNC.Test.service.AlbumService;
import com.BCNC.Test.service.AlbumServiceImpl;
import com.BCNC.Test.service.PhotoServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RestController
@RequestMapping("/albums")
@Controller
public class AlbumController {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumServiceImpl albumServiceImpl;

    @PostMapping("/enrichAndSave")
    public ResponseEntity<String> enrichAndSaveAlbums() {
        try {
            albumRepository.saveAll(albumServiceImpl.enriching());
            return ResponseEntity.status(HttpStatus.OK).body("Almacenado en BDD");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error recuperando datos");
        }
    }

    @GetMapping("/enrich")
    public ResponseEntity<String> enrichAlbums() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //Convertimos en JSON para poder responder en String y aclarar el posible error de procesamiento del catch
                return new ResponseEntity<>(objectMapper.writeValueAsString(albumServiceImpl.enriching()), HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error al convertir a JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error recuperando datos");
        }
    }

    @GetMapping("/getAlbumsFromDB")
    public ResponseEntity<List<Album>> getAllAlbumsFromDb() {
        List<Album> albums = albumRepository.findAll();
        if (!albums.isEmpty()){
            return ResponseEntity.ok().body(albums);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(albums);

        }
    }

}
