package com.hardcodedlambda.media.service;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.model.MediaSearchQuery;
import com.hardcodedlambda.media.repository.MediaRepository;
import com.hardcodedlambda.media.util.TestDataGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.ZonedDateTime;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaServiceTest {

    private static final String TITLE = "tile";
    private static final String EXISTENT_GENRE = "existent genre";
    private static final String NON_EXISTENT_GENRE = "non existent genre";

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaService mediaService;

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
    public void testSearchWhenQueryContainsOneProperty() {

        Media media = TestDataGenerator.validMediaWithoutId();
        media.setTitle(TITLE);
        media.setCreatedAt(ZonedDateTime.now());
        media.setUpdatedLastAt(ZonedDateTime.now());
        mediaRepository.save(media);

        MediaSearchQuery mediaSearchQuery = MediaSearchQuery.builder().title(TITLE).build();
        List<Media> searchResult = mediaService.search(mediaSearchQuery);

        assertEquals(1, searchResult.size());
        assertEquals(TITLE, searchResult.get(0).getTitle());
    }

    @Test
    public void testSearchWhenQueryContainsMultiplePropertiesButOnlyOneMatches() {

        Media media = TestDataGenerator.validMediaWithoutId();
        media.setTitle(TITLE);
        media.getGenre().setName(EXISTENT_GENRE);
        media.setCreatedAt(ZonedDateTime.now());
        media.setUpdatedLastAt(ZonedDateTime.now());
        mediaRepository.save(media);

        MediaSearchQuery mediaSearchQuery = MediaSearchQuery.builder().title(TITLE).genre(NON_EXISTENT_GENRE).build();
        List<Media> searchResult = mediaService.search(mediaSearchQuery);

        assertTrue(searchResult.isEmpty());
    }

    @Test
    public void testSearchWhenQueryContainsMultiplePropertiesAndAllMatch() {

        Media matchingMedia = TestDataGenerator.validMediaWithoutId();
        matchingMedia.setTitle(TITLE);
        matchingMedia.getGenre().setName(EXISTENT_GENRE);
        matchingMedia.setCreatedAt(ZonedDateTime.now());
        matchingMedia.setUpdatedLastAt(ZonedDateTime.now());
        mediaRepository.save(matchingMedia);

        Media nonMatchingMedia = TestDataGenerator.validMediaWithoutId();
        nonMatchingMedia.setTitle(TITLE);
        nonMatchingMedia.getGenre().setName(NON_EXISTENT_GENRE);
        nonMatchingMedia.setCreatedAt(ZonedDateTime.now());
        nonMatchingMedia.setUpdatedLastAt(ZonedDateTime.now());
        mediaRepository.save(nonMatchingMedia);

        MediaSearchQuery mediaSearchQuery = MediaSearchQuery.builder().title(TITLE).genre(EXISTENT_GENRE).build();
        List<Media> searchResult = mediaService.search(mediaSearchQuery);

        assertEquals(1, searchResult.size());
        assertEquals(TITLE, searchResult.get(0).getTitle());
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