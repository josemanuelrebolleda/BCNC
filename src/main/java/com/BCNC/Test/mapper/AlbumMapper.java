package com.BCNC.Test.mapper;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.exception.InvalidAlbumDataException;
import com.BCNC.Test.model.AlbumDTO;
import com.BCNC.Test.model.PhotoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlbumMapper {
    public Album mapToAlbum(AlbumDTO albumDTO) {
        if (albumDTO == null) {
            throw new InvalidAlbumDataException("El AlbumDTO es nulo");
        }

        Album album = new Album();
        album.setId(albumDTO.getId());
        album.setTitle(albumDTO.getTitle());
        return album;
    }
    public List<Album> mapToAlbums(List<AlbumDTO> albumDTOs) {
        if (albumDTOs == null) {
            throw new InvalidAlbumDataException("La lista de AlbumDTO es nula");
        }

        return albumDTOs.stream()
                .map(albumDTO -> {
                    if (albumDTO == null) {
                        throw new InvalidAlbumDataException("Un AlbumDTO en la lista es nulo");
                    }
                    return mapToAlbum(albumDTO);
                })
                .collect(Collectors.toList());
    }
}
