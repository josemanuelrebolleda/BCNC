package com.BCNC.Test.integration;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.repository.AlbumRepository;
import com.BCNC.Test.service.AlbumServiceImpl;
import com.BCNC.Test.service.strategy.EnrichingStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlbumServiceImplIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AlbumServiceImpl albumService;

    @Autowired
    private AlbumRepository albumRepository;

    //Listas para usar en pruebas
    private static final List<Album> albums = new ArrayList<>();
    private static final List<Album> albumsEnriched = new ArrayList<>();
    private static final List<Photo> photos = new ArrayList<>();

    @BeforeEach
    public void integrationGenerateEnrichedAlbum() {
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

        // Establecemos el albumId en los objetos Photo
        photo1.setAlbumId(album.getId());
        photo2.setAlbumId(album.getId());

        albums.add(album);

        // Añadimos el objeto Album a la lista albums y lo guardamos en H2
        albumsEnriched.add(new Album(album));
        albumsEnriched.get(0).setPhotos(photos);
        albumRepository.deleteAll();
        albumRepository.saveAll(albumsEnriched);

    }

    @Test
    public void integrationTestEnrichAndSaveAlbumsOk() {
        // Crear una estrategia de enriquecimiento que devuelva los álbumes enriquecidos
        EnrichingStrategy integrationTestEnrichingStrategy = new TestEnrichingStrategy(albumsEnriched);

        // Inyectar la estrategia de enriquecimiento en el servicio
        ReflectionTestUtils.setField(albumService, "enrichingStrategy", integrationTestEnrichingStrategy);

        // Mockear el método saveAll() del repositorio para que devuelva los álbumes enriquecidos
        when(albumRepository.saveAll(any())).thenReturn(albumsEnriched);

        ResponseEntity<String> response = albumService.enrichAndSaveAlbums();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Almacenado en BDD", response.getBody());

        // Luego de guardar los álbumes, los recuperamos para verificar que se guardaron correctamente
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/albums/getAlbumsFromDB", String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedBody = null;
        try {
            expectedBody = objectMapper.writeValueAsString(albumsEnriched);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertEquals(expectedBody, getResponse.getBody());
    }
    @Test
    public void integrationTestGetAlbumsFromDBOk() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity("/albums/getAlbumsFromDB", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        List<Album> actualAlbums = objectMapper.readValue(response.getBody(), new TypeReference<List<Album>>(){});
        assertEquals(albumsEnriched, actualAlbums);
    }

//    @Test
//    public void integrationTestEnrichAlbumsOk() throws JsonProcessingException {
//        ResponseEntity<String> response = restTemplate.getForEntity("/albums/enrichAlbums", String.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String expectedBody = objectMapper.writeValueAsString(albums);
//        assertEquals(expectedBody, response.getBody());
//    }

    @Test
    public void integrationTestEnriching() {
        ResponseEntity<Album[]> response = restTemplate.getForEntity("/albums/enriching", Album[].class);
        List<Album> result = Arrays.asList(response.getBody());

        assertEquals(albumsEnriched, result, "El resultado debe ser la lista de álbumes con las fotos correspondientes");
    }

    // Creamos un album para pruebas previamente
    @Test
    public void integrationTestEnrichAndSaveAlbumsThrowsException() {
        // Configurar el comportamiento del servicio para que lance una excepción
        doThrow(new RuntimeException()).when(albumService).enrichAndSaveAlbums();

        // Verificar que se lanza la excepción
        assertThrows(RuntimeException.class, () -> albumService.enrichAndSaveAlbums());
    }

    @Test
    public void integrationTestGetAlbumsFromDBThrowsException() {
        // Configurar el comportamiento del servicio para que lance una excepción
        doThrow(new RuntimeException()).when(albumService).getAlbumsFromDB();

        // Verificar que se lanza la excepción
        assertThrows(RuntimeException.class, () -> albumService.getAlbumsFromDB());
    }

    @Test
    public void integrationTestEnrichAlbumsThrowsException() {
        // Configurar el comportamiento del servicio para que lance una excepción
        doThrow(new RuntimeException()).when(albumService).enrichAlbums();

        // Verificar que se lanza la excepción
        assertThrows(RuntimeException.class, () -> albumService.enrichAlbums());
    }

    @Test
    public void integrationTestEnrichingThrowsException() {
        // Configurar el comportamiento del servicio para que lance una excepción
        doThrow(new RuntimeException()).when(albumService).enriching();

        // Verificar que se lanza la excepción
        assertThrows(RuntimeException.class, () -> albumService.enriching());
    }
}