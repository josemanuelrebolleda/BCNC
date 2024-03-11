package com.BCNC.Test.integration;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.exception.EnrichAlbumsException;
import com.BCNC.Test.exception.EnrichAndSaveAlbumsException;
import com.BCNC.Test.exception.GetAlbumsFromDBException;
import com.BCNC.Test.repository.AlbumRepository;
import com.BCNC.Test.service.AlbumServiceImpl;
import com.BCNC.Test.service.strategy.EnrichingStrategyImpl;
import com.BCNC.Test.tools.ToolsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class AlbumServiceImplIntegrationTest {

    @Autowired
    private AlbumServiceImpl albumServiceImpl;
    @Autowired
    private ToolsImpl tools;
    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private AlbumRepository albumRepositoryMock;
    @MockBean
    EnrichingStrategyImpl enrichingStrategyMock;

    private static final List<Album> enrichedAlbums = new ArrayList<>();
    @BeforeEach
    public void integrationGenerateEnrichedAlbum() {
        enrichedAlbums.addAll(tools.setup());
    }
    @Test
    public void integrationTestEnrichAndSaveAlbumsOK() {

        when(enrichingStrategyMock.enrich()).thenReturn(enrichedAlbums);

        ResponseEntity<String> result = albumServiceImpl.enrichAndSaveAlbums();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Almacenado en BDD", result.getBody());
        verify(albumRepositoryMock, times(1)).saveAll(enrichedAlbums);
    }

    @Test
    public void integrationTestEnrichAndSaveAlbumsKoEmptyList() {
        when(enrichingStrategyMock.enrich()).thenReturn(Collections.emptyList());

        assertThrows(EnrichAndSaveAlbumsException.class, () -> albumServiceImpl.enrichAndSaveAlbums());
        verify(albumRepositoryMock, times(1)).saveAll(anyList());
    }
    @Test
    public void integrationTestEnrichAndSaveAlbumsKoException() {
        when(enrichingStrategyMock.enrich()).thenThrow(EnrichAndSaveAlbumsException.class);

        assertThrows(EnrichAndSaveAlbumsException.class, () -> albumServiceImpl.enrichAndSaveAlbums());
    }

    @Test
    public void integrationTestGetAlbumsFromDBGetAlbumsFromDBOK() throws JsonProcessingException {
        when(albumRepositoryMock.findAll()).thenReturn(enrichedAlbums);

        ResponseEntity<String> response = albumServiceImpl.getAlbumsFromDB();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String album1Json = objectMapper.writeValueAsString(enrichedAlbums.get(0));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(album1Json));
    }
    @Test
    public void integrationTestGetAlbumsFromDBGetAlbumsFromDBKo() {
        when(albumRepositoryMock.findAll()).thenThrow(GetAlbumsFromDBException.class);

        assertThrows(GetAlbumsFromDBException.class, () -> albumServiceImpl.getAlbumsFromDB());
    }
    @Test
    public void integrationTestEnrichAlbumsOK() throws JsonProcessingException {
        when(enrichingStrategyMock.enrich()).thenReturn(enrichedAlbums);

        ResponseEntity<String> response = albumServiceImpl.enrichAlbums();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String expectedBody = objectMapper.writeValueAsString(enrichedAlbums);
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void integrationTestEnrichAlbumsKo() {
        when(enrichingStrategyMock.enrich()).thenThrow(EnrichAlbumsException.class);

        assertThrows(EnrichAlbumsException.class, () -> albumServiceImpl.enrichAlbums());
    }}