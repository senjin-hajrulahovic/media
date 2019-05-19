package com.hardcodedlambda.media.rest;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.repository.MediaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/media")
@AllArgsConstructor
public class MediaController {

    private MediaRepository mediaRepository;

    @PostMapping("/")
    public ResponseEntity<Media> create(Media receivedMedia) {
        Media persistedMedia = mediaRepository.save(receivedMedia);
        return ResponseEntity.ok(persistedMedia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> read(@PathVariable long id) {
        Media retrievedMedia = mediaRepository.getOne(id);
        return ResponseEntity.ok(retrievedMedia);
    }

    @PutMapping("/")
    public ResponseEntity<Media> update(Media receivedMedia) {
        Media persistedMedia = mediaRepository.save(receivedMedia);
        return ResponseEntity.ok(persistedMedia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Media> delete(@PathVariable long id) {
        mediaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public List<Media> getAll() {
        return mediaRepository.findAll();
    }
}
