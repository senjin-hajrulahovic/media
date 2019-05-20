package com.hardcodedlambda.media.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.repository.MediaRepository;
import com.hardcodedlambda.media.util.TestDataGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MediaRepository mediaRepository;

    private static JdbcDatabaseContainer postgresContainer = new PostgreSQLContainer()
            .withDatabaseName("media")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("postgres/schema.sql");

    @BeforeClass
    public static void init() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
    }

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

    @After
    public void cleanup() {
        mediaRepository.deleteAll();
    }

    @AfterClass
    public static void shutdown() {
        postgresContainer.stop();
    }
}