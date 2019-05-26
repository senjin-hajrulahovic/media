package com.hardcodedlambda.media.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaAssignmentCreationRequest {

    @NotEmpty
    private List<MediaAssignment> mediaAssignmentList;
    @NotNull
    private ZonedDateTime expiresAt;
}
