package com.hardcodedlambda.media.repository;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.model.MediaAssignment;
import com.hardcodedlambda.media.model.MediaAssignmentId;
import com.hardcodedlambda.media.model.Subscriber;
import com.hardcodedlambda.media.util.TestDataGenerator;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Ignore
public class MediaAssignmentRepositoryTest {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaAssignmentRepository mediaAssignmentRepository;

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

    @Test(expected = DataIntegrityViolationException.class)
//    @Transactional
    public void shouldThrowExceptionIfMediaOrUserDoesNotExist() {

        MediaAssignmentId mediaAssignmentId = MediaAssignmentId.builder()
                .media(Media.builder().id(2L).build())
                .subscriber(Subscriber.builder().id(2L).build())
                .build();

        MediaAssignment mediaAssignment = MediaAssignment.builder()
                .id(mediaAssignmentId)
                .expiresAt(ZonedDateTime.now())
                .build();

        mediaAssignmentRepository.save(mediaAssignment);

    }

    @Test
    @Transactional

    public void shouldPersistAssignmentIfMediaAndUserDoExist() {
        Media media = TestDataGenerator.validMediaWithoutId();
        Media persistedMedia = mediaRepository.save(media);

        Subscriber subscriber = Subscriber.builder().name("subscriber").build();
        Subscriber persistedSubscriber = userRepository.save(subscriber);

        MediaAssignmentId mediaAssignmentId = MediaAssignmentId.builder()
                .media(persistedMedia)
                .subscriber(persistedSubscriber)
                .build();

        MediaAssignment mediaAssignment = MediaAssignment.builder()
                .id(mediaAssignmentId)
                .expiresAt(ZonedDateTime.now())
                .build();

        mediaAssignmentRepository.save(mediaAssignment);

        MediaAssignment result = mediaAssignmentRepository.getOne(mediaAssignmentId);

        assertEquals(persistedMedia.getId(), result.getId().getMedia().getId());
        assertEquals(persistedSubscriber.getId(), result.getId().getSubscriber().getId());
    }

    @Test
    @Transactional

    public void shouldPersistAssignmentIfOnlyMediaAndSubscriberIdsArePassedAndMediaAnUserDoExist() {
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

        mediaAssignmentRepository.save(mediaAssignment);

        MediaAssignment result = mediaAssignmentRepository.getOne(mediaAssignmentId);

        assertEquals(persistedMedia.getId(), result.getId().getMedia().getId());
        assertEquals(persistedSubscriber.getId(), result.getId().getSubscriber().getId());
    }

//    @Transactional

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