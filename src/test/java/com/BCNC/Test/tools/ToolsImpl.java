package com.BCNC.Test.tools;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



@Component
public class ToolsImpl implements Tools{
    @Autowired
    private AlbumRepository albumRepository;
    public List<Album> setup() {
        // Crear dos objetos Photo
        List<Photo> photos = new ArrayList<>();
        List<Album> albums = new ArrayList<>();
        Photo photo1 = new Photo();
        photo1.setId(1L);
        photo1.setTitle("Título de la Foto 1");
        photo1.setUrl("URL de la Foto 1");

        Photo photo2 = new Photo();
        photo2.setId(2L);
        photo2.setTitle("Título de la Foto 2");
        photo2.setUrl("URL de la Foto 2");

        // Añadimos los objetos Photo a la lista photos
        photos.add(photo1);
        photos.add(photo2);

        // Creamos un objeto Album y establecer su lista de Photo a la lista photos
        Album album = new Album();
        album.setId(1L);
        album.setTitle("Título del Álbum");

        // Establecemos el albumId en los objetos Photo
        photo1.setAlbumId(album.getId());
        photo2.setAlbumId(album.getId());
        album.setPhotos(photos);

        albums.add(album);

        // Añadimos el objeto Album a la lista albums y lo guardamos en H2
        albumRepository.deleteAll();
        albumRepository.saveAll(albums);
        return albums;
    }
}
