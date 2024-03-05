package com.BCNC.Test.mapper;

import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.model.PhotoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class PhotoMapper {
    public Photo mapToPhoto(PhotoDTO photoDTO) {
        Photo photo = new Photo();
        photo.setId(photoDTO.getId());
        photo.setTitle(photoDTO.getTitle());
        photo.setUrl(photoDTO.getUrl());
        photo.setThumbnailUrl(photoDTO.getThumbnailUrl());
        photo.setAlbumId(photoDTO.getAlbumId());
        return photo;
    }

    public List<Photo> mapToPhotos(List<PhotoDTO> photoDTOs) {
        return photoDTOs.stream()
                .map(this::mapToPhoto)
                .collect(Collectors.toList());
    }
}