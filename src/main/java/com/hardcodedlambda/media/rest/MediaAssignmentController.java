package com.hardcodedlambda.media.rest;

import com.hardcodedlambda.media.model.MediaAssignment;
import com.hardcodedlambda.media.model.MediaAssignmentCreationRequest;
import com.hardcodedlambda.media.service.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/media/assignment")
@AllArgsConstructor
public class MediaAssignmentController {

    private MediaService mediaService;

    @PostMapping
    public ResponseEntity<List<MediaAssignment>> create(@RequestBody @Valid MediaAssignmentCreationRequest request,
                                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().build();
        } else {
            return ResponseEntity.ok(mediaService.createAssignments(request));
        }
    }

    @GetMapping
    public ResponseEntity<List<MediaAssignment>> getAllExpiredMappings() {
        return ResponseEntity.ok(mediaService.getAllExpiredMediaAssignments(ZonedDateTime.now()));
    }
}
