package com.BCNC.Test.unitary;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.entity.Photo;
import com.BCNC.Test.exception.EnrichAndSaveAlbumsException;
import com.BCNC.Test.exception.EnrichAlbumsException;
import com.BCNC.Test.integration.TestEnrichingStrategy;
import com.BCNC.Test.repository.AlbumRepository;
import com.BCNC.Test.service.AlbumServiceImpl;
import com.BCNC.Test.service.PhotoServiceImpl;
import com.BCNC.Test.service.strategy.EnrichingStrategy;
import com.BCNC.Test.service.strategy.EnrichingStrategyImpl;
import com.BCNC.Test.tools.ToolsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AlbumServiceImplTest {
    @Autowired
    private AlbumServiceImpl albumServiceImpl;
    @Autowired
    private ToolsImpl tools;
    @MockBean
    private AlbumRepository albumRepositoryMock;

    @MockBean
    private PhotoServiceImpl photoService;

    @Mock
    private AlbumServiceImpl albumServiceMock;

    @MockBean
    EnrichingStrategyImpl enrichingStrategyMock;

    //Listas para usar en pruebas
    private static final List<Album> albums = new ArrayList<>();
    private static final List<Album> enrichedAlbums = new ArrayList<>();
    private static final List<Photo> photos = new ArrayList<>();


    @BeforeEach
    public void setup() {
        Mockito.reset(enrichingStrategyMock, albumRepositoryMock);
        enrichedAlbums.addAll(tools.setup());
    }
    @Test
    public void testEnrichAndSaveAlbumsOk() {
        when(enrichingStrategyMock.enrich()).thenReturn(enrichedAlbums);
        when(albumRepositoryMock.saveAll(any())).thenReturn(enrichedAlbums);

        ResponseEntity<String> response = albumServiceImpl.enrichAndSaveAlbums();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Almacenado en BDD", response.getBody());
    }
    @Test
    public void testEnrichAndSaveAlbumsKoEmptyList() {
        // Hacer que el método enriching() devuelva una lista vacía cuando se llame
        when(enrichingStrategyMock.enrich()).thenReturn(new ArrayList<>());

        // Llamar al método enrichAndSaveAlbums() y verificar que lanza una excepción EnrichAndSaveAlbumsException
        assertThrows(EnrichAndSaveAlbumsException.class, () -> albumServiceImpl.enrichAndSaveAlbums());
    }
    @Test
    public void testEnrichAndSaveAlbumsKoException() {
        // Hacer que el método enrich() lance una excepción cuando se llame
        when(enrichingStrategyMock.enrich()).thenThrow(EnrichAndSaveAlbumsException.class);

        // Llamar al método enrichAndSaveAlbums() y verificar que lanza una excepción EnrichAndSaveAlbumsException
        assertThrows(EnrichAndSaveAlbumsException.class, () -> albumServiceImpl.enrichAndSaveAlbums());
    }
    @Test
    public void testGetAlbumsFromDBOk() throws JsonProcessingException {
        when(albumRepositoryMock.findAll()).thenReturn(albums);

        ResponseEntity<String> response = albumServiceImpl.getAlbumsFromDB();
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedBody = objectMapper.writeValueAsString(albums);
        assertEquals(expectedBody, response.getBody());
    }
    @Test
    public void testGetAlbumsFromDBKo() {
        when(albumRepositoryMock.findAll()).thenThrow(new RuntimeException());

        Exception exception = assertThrows(RuntimeException.class, () -> albumServiceImpl.getAlbumsFromDB());
        assertNotNull(exception);
    }
    @Test
    public void testEnrichAlbumsOk() throws JsonProcessingException {
        // Crear una estrategia de enriquecimiento que devuelva los álbumes enriquecidos
        EnrichingStrategy testEnrichingStrategy = new TestEnrichingStrategy(enrichedAlbums);

        // Inyectar la estrategia de enriquecimiento en el servicio
        ReflectionTestUtils.setField(albumServiceImpl, "enrichingStrategy", testEnrichingStrategy);

        // Crear una respuesta esperada
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedBody = objectMapper.writeValueAsString(enrichedAlbums);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(expectedBody, headers, HttpStatus.OK);

        // Llamar al método enrichAlbums() y verificar que devuelve una respuesta con el estado HTTP 200 OK
        // y el cuerpo de la respuesta como la cadena JSON de los álbumes enriquecidos
        ResponseEntity<String> response = albumServiceImpl.enrichAlbums();
        assertEquals(expectedResponse, response);
    }
    @Test
    public void testEnrichAlbumsKo() {
        // Hacer que el método enrich() lance una excepción cuando se llame
        when(enrichingStrategyMock.enrich()).thenThrow(EnrichAlbumsException.class);

        // Llamar al método enrichAlbums() y verificar que lanza una excepción EnrichAlbumsException
        assertThrows(EnrichAlbumsException.class, () -> albumServiceImpl.enrichAlbums());
    }
//        // Hacer que el método enrich() lance una excepción cuando se llame
//        when(enrichingStrategyMock.enrich()).thenThrow(EnrichAlbumsException.class);
//
//        // Llamar al método enrichAlbums() y verificar que devuelve una respuesta con el estado HTTP 400 BAD REQUEST
//        ResponseEntity<String> response = albumServiceImpl.enrichAlbums();
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
    @Test
    public void testEnriching() {
        EnrichingStrategy testEnrichingStrategy = new TestEnrichingStrategy(enrichedAlbums);

        // Inyectar la estrategia de enriquecimiento en el servicio
        ReflectionTestUtils.setField(albumServiceImpl, "enrichingStrategy", testEnrichingStrategy);

        // Llamar al método enriching() y verificar que devuelve testAlbums
        assertEquals(enrichedAlbums, albumServiceImpl.enriching());
    }

}
