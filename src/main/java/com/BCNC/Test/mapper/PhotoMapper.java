package com.BCNC.Test.mapper;

import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.exception.InvalidPhotoDataException;
import com.BCNC.Test.model.PhotoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class PhotoMapper {
    public Photo mapToPhoto(PhotoDTO photoDTO) {
        if (photoDTO == null) {
            throw new InvalidPhotoDataException("El PhotoDTO es nulo");
        }

        Photo photo = new Photo();
        photo.setId(photoDTO.getId());
        photo.setTitle(photoDTO.getTitle());
        photo.setUrl(photoDTO.getUrl());
        photo.setThumbnailUrl(photoDTO.getThumbnailUrl());
        photo.setAlbumId(photoDTO.getAlbumId());
        return photo;
    }
    public List<Photo> mapToPhotos(List<PhotoDTO> photoDTOs) {
        if (photoDTOs == null) {
            throw new InvalidPhotoDataException("La lista de PhotoDTO es nula");
        }

        return photoDTOs.stream()
                .map(photoDTO -> {
                    if (photoDTO == null) {
                        throw new InvalidPhotoDataException("Un PhotoDTO en la lista es nulo");
                    }
                    return mapToPhoto(photoDTO);
                })
                .collect(Collectors.toList());
    }}