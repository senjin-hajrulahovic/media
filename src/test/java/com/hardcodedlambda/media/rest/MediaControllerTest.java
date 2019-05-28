package com.hardcodedlambda.media.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.repository.MediaRepository;
import com.hardcodedlambda.media.util.TestDataGenerator;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.ZonedDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private HikariDataSource dataSource;

    private static JdbcDatabaseContainer postgresContainer = (JdbcDatabaseContainer)(new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("media")
            .withUsername("postgres")
            .withPassword("postgres"))
            .withStartupTimeout(Duration.ofSeconds(10000));
//            .withInitScript("postgres/schema.sql");

    @BeforeClass
    public static void init() {
        postgresContainer.start();

        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
//        System.setProperty("spring.datasource.hikari.maxLifetime", "10");
//        System.setProperty("spring.datasource.hikari.minimumIdle", "10");
    }

    // media creation

    @Test
    public void postEndpointShouldReturn200IfRequestBodyIsValid() throws Exception {
        Media validMediaWithoutId = TestDataGenerator.validMediaWithoutId();

        mockMvc.perform(
                post("/media")
                    .content(mapper.writeValueAsString(validMediaWithoutId))
                    .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void postEndpointShouldReturn422IfRequestBodyIsValidButHasId() throws Exception {
        Media validMediaWithId = TestDataGenerator.validMediaWithId();

        mockMvc.perform(
                post("/media")
                    .content(mapper.writeValueAsBytes(validMediaWithId))
                    .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void postEndpointShouldReturn422IfRequestBodyHasInvalidNestedProperty() throws Exception {
        Media validMediaWithId = TestDataGenerator.validMediaWithoutId();
        validMediaWithId.getArtist().setName(null);

        mockMvc.perform(
                post("/media")
                        .content(mapper.writeValueAsBytes(validMediaWithId))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    // media update

    @Test
    public void putEndpointShouldReturn200IfRequestBodyIsValid() throws Exception {
        Media validMediaWithIdToBePersisted = TestDataGenerator.validMediaWithId();
        validMediaWithIdToBePersisted.setCreatedAt(ZonedDateTime.now());
        validMediaWithIdToBePersisted.setUpdatedLastAt(ZonedDateTime.now());

        Media persistedMedia = mediaRepository.save(validMediaWithIdToBePersisted);

        Media validMediaWithIdToSentAsBody = TestDataGenerator.validMediaWithId();
        validMediaWithIdToSentAsBody.setTitle("updated title");
        validMediaWithIdToSentAsBody.getArtist().setName("updated arsits");
        validMediaWithIdToSentAsBody.setId(persistedMedia.getId());

        mockMvc.perform(
                put("/media")
                        .content(mapper.writeValueAsString(validMediaWithIdToSentAsBody))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validMediaWithIdToSentAsBody.getId()))
                .andExpect(jsonPath("title").value(validMediaWithIdToSentAsBody.getTitle()))
                .andExpect(jsonPath("artist.name").value(validMediaWithIdToSentAsBody.getArtist().getName()));
    }

    @Test
    public void putEndpointShouldReturn422IfRequestBodyIsValidButHasNoId() throws Exception {
        Media validMediaWithId = TestDataGenerator.validMediaWithoutId();

        mockMvc.perform(
                put("/media")
                        .content(mapper.writeValueAsBytes(validMediaWithId))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getShouldReturnMediaIfMediaWithIdExists() throws Exception {
        Media mediaToBePersisted = TestDataGenerator.validMediaWithoutId();
        mediaToBePersisted.setCreatedAt(ZonedDateTime.now());
        mediaToBePersisted.setUpdatedLastAt(ZonedDateTime.now());
        Media persistedMedia = mediaRepository.save(mediaToBePersisted);

        mockMvc.perform(get("/media/" + persistedMedia.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(persistedMedia.getId()))
                .andExpect(jsonPath("$.title").value(persistedMedia.getTitle()))
                .andExpect(jsonPath("$.artist.name").value(persistedMedia.getArtist().getName()))
                .andExpect(jsonPath("$.genre.name").value(persistedMedia.getGenre().getName()))
                .andExpect(jsonPath("$.originatingCountry.name").value(persistedMedia.getOriginatingCountry().getName()))
                .andExpect(jsonPath("$.publisher.name").value(persistedMedia.getPublisher().getName()));
    }

    @Test
    public void getShouldReturn404fMediaWithIdDoesNotExists() throws Exception {
        mockMvc.perform(get("/media/1")).andExpect(status().isNotFound());
    }

    @Test
    public void attemptToDeleteExistentMediaShouldResultIn200() throws Exception {
        Media mediaToBePersisted = TestDataGenerator.validMediaWithoutId();
        mediaToBePersisted.setCreatedAt(ZonedDateTime.now());
        mediaToBePersisted.setUpdatedLastAt(ZonedDateTime.now());
        Media persistedMedia = mediaRepository.save(mediaToBePersisted);

        mockMvc.perform(delete("/media/" + persistedMedia.getId())).andExpect(status().isOk());
    }

    @Test
    public void attemptToDeleteNonExistentMediaShouldResultIn204() throws Exception {
        mockMvc.perform(delete("/media/1")).andExpect(status().isNoContent());
    }

    @After
    @Transactional
    public void cleanup() {
        mediaRepository.deleteAll();

//        dataSource.close();
    }

    @AfterClass
    public static void shutdown() {
        postgresContainer.stop();
    }
}