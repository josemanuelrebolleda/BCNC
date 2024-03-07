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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        return albumServiceImpl.enrichAndSaveAlbums();
    }

    @GetMapping("/enrichAlbums")
    public ResponseEntity<String> enrichAlbums() {
        return albumServiceImpl.enrichAlbums();
    }

    @GetMapping("/getAlbumsFromDB")
    public ResponseEntity<String> getAlbumsFromDB() {
        return albumServiceImpl.getAlbumsFromDB();
    }

}
