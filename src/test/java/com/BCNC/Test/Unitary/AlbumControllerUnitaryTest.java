package com.BCNC.Test.Unitary;

import static org.mockito.Mockito.*;

import com.BCNC.Test.controller.AlbumController;
import com.BCNC.Test.entity.Album;
import com.BCNC.Test.repository.AlbumRepository;
import com.BCNC.Test.service.AlbumServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AlbumControllerUnitaryTest {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumServiceImpl albumServiceImpl;

//    public AlbumControllerUnitaryTest(AlbumRepository albumRepository, AlbumServiceImpl albumServiceImpl) {
//        this.albumRepository = albumRepository;
//        this.albumServiceImpl = albumServiceImpl;
//    }

    @BeforeEach
    public void setUpDatabase() {
        // Inicializamos la base de datos con datos de prueba
        Album album1 = new Album();
        album1.setId(1L);
        album1.setTitle("Test Album 1");
        albumRepository.save(album1);

        Album album2 = new Album();
        album2.setId(2L);
        album2.setTitle("Test Album 2");
        albumRepository.save(album2);
    }

    @Test
    public void testEnrichAndSaveAlbums_Success() {
        // Configuración de Mocks
        AlbumRepository albumRepository = mock(AlbumRepository.class);
        when(albumRepository.saveAll(anyList())).thenReturn(null); // Simulamos que el guardado en la base de datos tiene éxito

        // Creación del controlador
        AlbumController albumController = new AlbumController();

        // Ejecución del método bajo prueba
        ResponseEntity<String> response = albumController.enrichAndSaveAlbums();

        // Verificación de la respuesta
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().equals("Almacenado en BDD");
    }

    @Test
    public void testEnrichAndSaveAlbums_Failure() {
        // Configuración de Mocks
        AlbumRepository albumRepository = mock(AlbumRepository.class);
        when(albumRepository.saveAll(anyList())).thenThrow(new RuntimeException()); // Simulamos que ocurre un error al guardar en la base de datos

        // Creación del controlador
        AlbumController albumController = new AlbumController();

        // Ejecución del método bajo prueba
        ResponseEntity<String> response = albumController.enrichAndSaveAlbums();

        // Verificación de la respuesta
        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody().equals("Error recuperando datos");
    }

    @Test
    public void testEnrichAlbums_Success() throws JsonProcessingException {
        // Mock del objeto objectMapper
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Configuramos el comportamiento del mock para devolver un JSON
        when(objectMapper.writeValueAsString(any())).thenReturn("[{\"id\":1,\"title\":\"Album 1\"},{\"id\":2,\"title\":\"Album 2\"}]");

        // Mock del servicio de álbumes
        AlbumServiceImpl AlbumServiceImpl = mock(AlbumServiceImpl.class);
        // Configuramos el comportamiento del mock para devolver una lista de álbumes
        when(AlbumServiceImpl.enriching()).thenReturn((ArrayList<Album>) Arrays.asList(new Album(1L, "Album 1"), new Album(2L, "Album 2")));

        // Creamos el controlador con los mocks
        AlbumController albumController = new AlbumController();

        // Ejecutamos el método bajo prueba
        ResponseEntity<String> response = albumController.enrichAlbums();

        // Verificamos que la respuesta sea la esperada
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().equals("[{\"id\":1,\"title\":\"Album 1\"},{\"id\":2,\"title\":\"Album 2\"}]");
    }

    @Test
    public void testEnrichAlbums_JsonProcessingException() throws JsonProcessingException {
        // Mock del objeto objectMapper
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Configuramos el comportamiento del mock para lanzar una excepción al convertir a JSON
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        // Mock del servicio de álbumes
        AlbumServiceImpl AlbumServiceImpl = mock(AlbumServiceImpl.class);

        // Creamos el controlador con los mocks
        AlbumController albumController = new AlbumController();

        // Ejecutamos el método bajo prueba
        ResponseEntity<String> response = albumController.enrichAlbums();

        // Verificamos que la respuesta sea la esperada
        assert response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        assert response.getBody().equals("Error al convertir a JSON");
    }

}
