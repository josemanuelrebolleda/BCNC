package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.model.AlbumDTO;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface AlbumService {
    ResponseEntity<List<Album>> getAlbumsFromDB();
    ResponseEntity<String> enrichAlbums();
    ResponseEntity<List<Album>> enrichAndSave();
    public List<Album> enriching();



}
