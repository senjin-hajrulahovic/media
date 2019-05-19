package com.hardcodedlambda.media.rest;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.repository.MediaRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaControllerTest {

    @Autowired
    private MediaRepository mediaRepository;

    private static JdbcDatabaseContainer postgresContainer = new PostgreSQLContainer()
            .withDatabaseName("media")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("postgres/schema.sql");

    @BeforeClass
    public static void init() throws IOException, InterruptedException {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
    }

    @Test
    public void testRepository() {
        mediaRepository.save(new Media());

        Media retrievedMedia = mediaRepository.getOne(1L);

        assertNotNull(retrievedMedia);
    }

    @AfterClass
    public static void shutdown() {
        postgresContainer.stop();
    }
}