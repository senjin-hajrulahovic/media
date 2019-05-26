package com.hardcodedlambda.media.repository;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.model.MediaAssignment;
import com.hardcodedlambda.media.model.MediaAssignmentId;
import com.hardcodedlambda.media.model.Subscriber;
import com.hardcodedlambda.media.util.TestDataGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaAssignmentRepositoryTest {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaAssignmentRepository mediaAssignmentRepository;

    private static JdbcDatabaseContainer postgresContainer = new PostgreSQLContainer()
            .withDatabaseName("media")
            .withUsername("postgres")
            .withPassword("postgres");
//            .withInitScript("postgres/schema.sql");

    @BeforeClass
    public static void init() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
    }

    @Test(expected = DataIntegrityViolationException.class)
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

    @After
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