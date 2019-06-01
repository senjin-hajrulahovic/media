package com.hardcodedlambda.media.model;

import com.hardcodedlambda.media.model.validation.OnCreate;
import com.hardcodedlambda.media.model.validation.OnUpdate;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.ZonedDateTime;

@Entity
//@Document(indexName = "media", type = "media")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    private Artist artist;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    private Publisher publisher;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    private Country originatingCountry;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    private Genre genre;

    @NotNull
    private ZonedDateTime publishingDate;

    @Null(groups = OnCreate.class)
    private ZonedDateTime createdAt;

    @Null(groups = OnCreate.class)
    private ZonedDateTime updatedLastAt;
}
