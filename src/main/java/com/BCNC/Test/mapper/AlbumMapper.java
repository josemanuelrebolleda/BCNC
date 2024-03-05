package com.BCNC.Test.mapper;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.model.AlbumDTO;
import com.BCNC.Test.model.PhotoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlbumMapper {
    public Album mapToAlbum(AlbumDTO albumDTO) {
        Album album = new Album();
        album.setId(albumDTO.getId());
        album.setTitle(albumDTO.getTitle());
        return album;
    }
    public List<Album> mapToAlbums(List<AlbumDTO> photoDTOs) {
        return photoDTOs.stream()
                .map(this::mapToAlbum)
                .collect(Collectors.toList());
    }
}
