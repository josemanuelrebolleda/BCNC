package com.BCNC.Test.integration;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AlbumControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlbumRepository albumRepository;

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
    public void testGetAllAlbumsFromDb() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/albums/getAlbumsFromDB")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").exists());
    }
}
