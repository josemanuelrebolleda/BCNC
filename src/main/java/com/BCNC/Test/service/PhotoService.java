package com.BCNC.Test.service;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.model.PhotoDTO;

import java.util.ArrayList;
import java.util.List;

public interface PhotoService {
    public List<Photo> loadPhotos();
}
