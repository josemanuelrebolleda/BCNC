package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;

import java.util.ArrayList;

public interface AlbumService {
    public ArrayList<Album> getAllAlbums();
    public void enrichAndSaveAlbums();
    public ArrayList<Album> enriching();


}
