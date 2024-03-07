package com.BCNC.Test.unitary;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.repository.AlbumRepository;
import com.BCNC.Test.service.AlbumServiceImpl;
import com.BCNC.Test.service.PhotoServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AlbumServiceImplTest {
    @Autowired
    private AlbumServiceImpl albumService;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private PhotoServiceImpl photoService;

    //Listas para usar en pruebas
    private static final List<Album> albums = new ArrayList<>();
    private static final List<Album> albumsEnriched = new ArrayList<>();
    private static final List<Photo> photos = new ArrayList<>();


    @Test
    public void testEnrichAndSaveAlbumsOk() {
        when(albumRepository.saveAll(any())).thenReturn(albums);

        ResponseEntity<String> response = albumService.enrichAndSaveAlbums();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Almacenado en BDD", response.getBody());
    }
    @Test
    public void testEnrichAndSaveAlbumsKo() {
        when(albumRepository.saveAll(any())).thenThrow(new RuntimeException());

        ResponseEntity<String> response = albumService.enrichAndSaveAlbums();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error recuperando datos", response.getBody());
    }

    @Test
    public void testGetAlbumsFromDBOk() throws JsonProcessingException {
        when(albumRepository.findAll()).thenReturn(albums);

        ResponseEntity<String> response = albumService.getAlbumsFromDB();
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedBody = objectMapper.writeValueAsString(albums);
        assertEquals(expectedBody, response.getBody());
    }
    @Test
    public void testGetAlbumsFromDBKo() {
        when(albumRepository.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<String> response = albumService.getAlbumsFromDB();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    public void testEnrichAlbumsOk() throws JsonProcessingException {
        when(photoService.loadPhotos()).thenReturn(photos);
        when(albumService.enriching()).thenReturn(albums);

        ResponseEntity<String> response = albumService.enrichAlbums();
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedBody = objectMapper.writeValueAsString(albums);
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testEnrichAlbumsKo() {
        when(photoService.loadPhotos()).thenThrow(new RuntimeException());

        ResponseEntity<String> response = albumService.enrichAlbums();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testEnriching() {
        // Mockear los métodos loadAlbums() y loadPhotos()
        when(albumService.loadAlbums()).thenReturn((albums));
        when(photoService.loadPhotos()).thenReturn((photos));
        when(albumRepository.findAll()).thenReturn(albums);

        // Llamar al método a probar
        List<Album> result = albumService.enriching();

        // Verificar el resultado
        assertEquals(albumsEnriched, result, "El resultado debe ser la lista de álbumes con las fotos correspondientes");
    }

    // Creamos un album para pruebas previamente
    @BeforeEach
    public void generateEnrichedAlbum() {
        // Crear dos objetos Photo
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

        albums.add(album);

        // Añadimos el objeto Album a la lista albums
        albumsEnriched.add(new Album(album));
        albumsEnriched.get(0).setPhotos(photos);

    }

}
