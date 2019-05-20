package com.hardcodedlambda.media.util;

import com.hardcodedlambda.media.model.*;

import java.time.ZonedDateTime;

public class TestDataGenerator {

    public static Media validMediaWithId() {
        Media media = validMediaWithoutId();
        media.setId(1L);
        return media;
    }

    public static Media validMediaWithoutId() {
        return Media.builder()
                .title("title")
                .artist(Artist.builder().name("artist").build())
                .originatingCountry(Country.builder().name("country").build())
                .genre(Genre.builder().name("genre").build())
                .publisher(Publisher.builder().name("publisher").build())
                .publishingDate(ZonedDateTime.now())
                .build();
    }
}
