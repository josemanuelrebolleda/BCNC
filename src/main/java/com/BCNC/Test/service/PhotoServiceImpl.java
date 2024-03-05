package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.mapper.PhotoMapper;
import com.BCNC.Test.model.AlbumDTO;
import com.BCNC.Test.model.PhotoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService{
    private static final Logger logger = LoggerFactory.getLogger(AlbumServiceImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PHOTOS_URL = "https://jsonplaceholder.typicode.com/photos";
    @Autowired
    PhotoMapper photoMapper;

    public List<Photo> loadPhotos() {
        RestTemplate restTemplate = new RestTemplate();
        PhotoDTO[] photosArray = null;
        try {
            photosArray = restTemplate.getForObject(PHOTOS_URL, PhotoDTO[].class);
        } catch (RestClientException e) {
            logger.error("Error cargando photos de repositorio", e);
            throw new RuntimeException("Error cargando photos de repositorio", e);
        }

        if (photosArray == null) {
            throw new RuntimeException("El repositorio devolvió datos nulos");
        }

        List<PhotoDTO> photos = Arrays.asList(photosArray);
        return mapToPhotos(photos);
    }

    private List<Photo> mapToPhotos(List<PhotoDTO> photoDTOs) {
        return photoMapper.mapToPhotos(photoDTOs);
    }
}
