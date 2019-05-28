package com.hardcodedlambda.media.service;

import com.hardcodedlambda.media.model.*;
import com.hardcodedlambda.media.repository.MediaAssignmentRepository;
import com.hardcodedlambda.media.repository.MediaRepository;
import com.hardcodedlambda.media.repository.UserRepository;
import com.hardcodedlambda.media.util.TestDataGenerator;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class MediaServiceTest {

    private static final String TITLE = "tile";
    private static final String EXISTENT_GENRE = "existent genre";
    private static final String NON_EXISTENT_GENRE = "non existent genre";

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaAssignmentRepository mediaAssignmentRepository;

    @Autowired
    private MediaService mediaService;

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

    @Test
    @Transactional
    @Ignore
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
    @Ignore
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

    @Test
    @Transactional
    @Ignore
    public void shouldCreateMediaAssignments() {

        Media media = TestDataGenerator.validMediaWithoutId();
        Media persistedMedia = mediaRepository.save(media);

        Subscriber subscriber = Subscriber.builder().name("subscriber").build();
        Subscriber persistedSubscriber = userRepository.save(subscriber);

        MediaAssignmentId mediaAssignmentId = MediaAssignmentId.builder()
                .media(Media.builder().id(persistedMedia.getId()).build())
                .subscriber(Subscriber.builder().id(persistedSubscriber.getId()).build())
                .build();

        MediaAssignment mediaAssignment = MediaAssignment.builder()
                .id(mediaAssignmentId)
                .expiresAt(ZonedDateTime.now())
                .build();

        ZonedDateTime now = ZonedDateTime.now();

        MediaAssignmentCreationRequest request = MediaAssignmentCreationRequest.builder()
                .mediaAssignmentList(Collections.singletonList(mediaAssignment))
                .expiresAt(now)
                .build();

        mediaService.createAssignments(request);

        List<MediaAssignment> persistedAssignments = mediaService.getAllMediaAssignments();

        assertEquals(1, persistedAssignments.size());
        assertEquals(persistedAssignments.get(0).getId().getMedia().getId(), persistedMedia.getId());
        assertEquals(persistedAssignments.get(0).getId().getSubscriber().getId(), persistedSubscriber.getId());
    }

    @Test
    @Transactional
    public void shouldReturnOnlyExpiredAssignments() {
        Media media = TestDataGenerator.validMediaWithoutId();
        Media persistedMedia = mediaRepository.save(media);

        Subscriber subscriber = Subscriber.builder().name("subscriber").build();
        Subscriber persistedSubscriber = userRepository.save(subscriber);

        MediaAssignmentId mediaAssignmentId = MediaAssignmentId.builder()
                .media(Media.builder().id(persistedMedia.getId()).build())
                .subscriber(Subscriber.builder().id(persistedSubscriber.getId()).build())
                .build();

        MediaAssignment mediaAssignment = MediaAssignment.builder()
                .id(mediaAssignmentId)
                .expiresAt(ZonedDateTime.now())
                .build();

        ZonedDateTime now = ZonedDateTime.now();

        MediaAssignmentCreationRequest request = MediaAssignmentCreationRequest.builder()
                .mediaAssignmentList(Collections.singletonList(mediaAssignment))
                .expiresAt(now)
                .build();

        mediaService.createAssignments(request);

        List<MediaAssignment> queryResultForTomorrow = mediaService.getAllExpiredMediaAssignments(now.plusDays(1));

        assertEquals(1, queryResultForTomorrow.size());
        assertEquals(queryResultForTomorrow.get(0).getId().getMedia().getId(), persistedMedia.getId());
        assertEquals(queryResultForTomorrow.get(0).getId().getSubscriber().getId(), persistedSubscriber.getId());

        List<MediaAssignment> queryResultForYesterday = mediaService.getAllExpiredMediaAssignments(now.minusDays(1));

        assertTrue(queryResultForYesterday.isEmpty());
    }

    @After
    @Transactional
    public void cleanup() {
        mediaAssignmentRepository.deleteAll();
        mediaRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterClass
    public static void shutdown() {
        postgresContainer.stop();
    }
}