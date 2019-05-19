package com.hardcodedlambda.media.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist artist;

    @ManyToOne(cascade = CascadeType.ALL)
    private Publisher publisher;

    @ManyToOne(cascade = CascadeType.ALL)
    private Country originatingCountry;

    @ManyToOne(cascade = CascadeType.ALL)
    private Genre genre;

    private ZonedDateTime publishingDate;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedLastAt;
}
