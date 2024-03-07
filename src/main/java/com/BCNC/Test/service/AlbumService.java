package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.model.AlbumDTO;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface AlbumService {
    public ResponseEntity<String> getAlbumsFromDB();
    public ResponseEntity<String> enrichAlbums();
    public ResponseEntity<String> enrichAndSaveAlbums();
    public List<Album> enriching();



}
