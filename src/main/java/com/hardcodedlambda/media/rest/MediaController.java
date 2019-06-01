package com.hardcodedlambda.media.rest;

import com.hardcodedlambda.media.model.MediaSearchQuery;
import com.hardcodedlambda.media.service.MediaService;
import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.model.validation.OnCreate;
import com.hardcodedlambda.media.model.validation.OnUpdate;
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
    public ResponseEntity<Media> create(@Validated({Default.class, OnCreate.class}) @RequestBody Media receivedMedia, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().build();
        } else {
            Media persistedMedia = mediaService.create(receivedMedia);
            return ResponseEntity.ok(persistedMedia);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> read(@PathVariable Long id) {

        return mediaService.get(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Media> update(@Validated({Default.class, OnUpdate.class}) @RequestBody Media receivedMedia, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().build();
        } else {
            Media persistedMedia = mediaService.update(receivedMedia);
            return ResponseEntity.ok(persistedMedia);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Media> delete(@PathVariable Long id) {

        return mediaService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public List<Media> getAll() {

        return mediaService.getAll();
    }

    @PostMapping("/search")
    public List<Media> search(MediaSearchQuery mediaSearchQuery) {
        return mediaService.search(mediaSearchQuery);
    }

    @GetMapping("/search/{term}")
    public Iterable<Media> search(@PathVariable String term) {
        return mediaService.fullTextSearch(term);
    }
}
