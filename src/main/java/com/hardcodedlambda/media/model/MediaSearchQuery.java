package com.hardcodedlambda.media.model;

import lombok.*;
import org.springframework.data.jpa.domain.Specification;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaSearchQuery {

    private String genre;
    private String originatingCountry;
    private String artist;
    private String title;
    private String publisher;

    public Specification<Media> getSpecification() {
        Specification<Media> specification = start();

        if (genre != null) {
            specification = specification.and(byGenre());
        }
        if (originatingCountry != null) {
            specification = specification.and(byOriginatingCountry());
        }
        if (artist != null) {
            specification = specification.and(byArtist());
        }
        if (title != null) {
            specification = specification.and(byTitle());
        }
        if (publisher != null) {
            specification = specification.and(byPublisher());
        }
        return specification;
    }

    private Specification<Media> start() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root);
    }

    private Specification<Media> byGenre() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("genre").get("name"), genre);
    }

    private Specification<Media> byOriginatingCountry() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("originatingCountry").get("name"), originatingCountry);
    }

    private Specification<Media> byArtist() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("artist").get("name"), artist);
    }

    private Specification<Media> byTitle() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title);
    }

    private Specification<Media> byPublisher() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("publisher").get("name"), publisher);
    }
}
