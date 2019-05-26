package com.hardcodedlambda.media.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaAssignment {

    @EmbeddedId
    private MediaAssignmentId id;

    private ZonedDateTime expiresAt;
}
