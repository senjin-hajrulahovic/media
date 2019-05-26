package com.hardcodedlambda.media.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class MediaAssignmentId implements Serializable {

    @ManyToOne
    private Media media;

    @ManyToOne
    private Subscriber subscriber;
}
