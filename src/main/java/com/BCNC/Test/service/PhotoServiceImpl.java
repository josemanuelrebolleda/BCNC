package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class PhotoServiceImpl implements PhotoService{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String photoUrl = "https://jsonplaceholder.typicode.com/photos";

    public ArrayList<Photo> getAllPhotos() {
        ResponseEntity<Photo[]> response = restTemplate.getForEntity(photoUrl, Photo[].class);

        // Verificamos si el cuerpo de la respuesta es nulo
        if (response.getBody() != null) {
            return new ArrayList<>(Arrays.asList(response.getBody()));
        } else {
            return null;
        }
    }
//JMRD
//    public Photo[] getPhotosByAlbumId(int albumId) {
//        ResponseEntity<Photo[]> response = restTemplate.getForEntity(photoUrl + "?albumId=" + String.valueOf(albumId), Photo[].class);
//        return response.getBody();
//    }

}
