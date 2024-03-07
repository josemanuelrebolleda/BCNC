package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.mapper.PhotoMapper;
import com.BCNC.Test.model.AlbumDTO;
import com.BCNC.Test.model.PhotoDTO;

import com.BCNC.Test.exception.LoadPhotosException;

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
    @Autowired
    private RestTemplate restTemplate;
    private static final String PHOTOS_URL = "https://jsonplaceholder.typicode.com/photos";
    @Autowired
    PhotoMapper photoMapper;

    public List<Photo> loadPhotos() {
        PhotoDTO[] photosArray = null;
        try {
            photosArray = restTemplate.getForObject(PHOTOS_URL, PhotoDTO[].class);
        } catch (RestClientException e) {
            throw new LoadPhotosException("Error cargando photos de repositorio", e);
        }

        if (photosArray == null) {
            throw new LoadPhotosException("El repositorio devolvió datos nulos", null);
        }

        return photoMapper.mapToPhotos(Arrays.asList(photosArray));    }
}
