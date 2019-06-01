package com.hardcodedlambda.media.elasticsearch;

import com.hardcodedlambda.media.model.Artist;
import com.hardcodedlambda.media.model.Country;
import com.hardcodedlambda.media.model.Genre;
import com.hardcodedlambda.media.model.Publisher;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Document(indexName = "media", type = "media")
@NoArgsConstructor
@AllArgsConstructor
public class MediaDocument {

    @Id
    private String id;

    private String title;

    private Artist artist;

    private Publisher publisher;

    private Country originatingCountry;

    private Genre genre;

    private ZonedDateTime publishingDate;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedLastAt;
}
