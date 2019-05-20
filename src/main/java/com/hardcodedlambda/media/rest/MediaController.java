package com.hardcodedlambda.media.rest;

import com.hardcodedlambda.media.MediaService;
import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.model.validation.OnCreate;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

@RestController
@RequestMapping("/media")
@AllArgsConstructor
public class MediaController {

    private MediaService mediaService;

    @PostMapping
    public ResponseEntity<Media> create(@Validated({Default.class, OnCreate.class}) @RequestBody Media receivedMedia,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().build();
        } else {
            Media persistedMedia = mediaService.create(receivedMedia);
            return ResponseEntity.ok(persistedMedia);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> read(@PathVariable Long id) {
        Media retrievedMedia = mediaService.get(id);
        return ResponseEntity.ok(retrievedMedia);
    }

    @PutMapping
    public ResponseEntity<Media> update(Media receivedMedia) {
        Media persistedMedia = mediaService.update(receivedMedia);
        return ResponseEntity.ok(persistedMedia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Media> delete(@PathVariable Long id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public List<Media> getAll() {
        return mediaService.getAll();
    }
}
