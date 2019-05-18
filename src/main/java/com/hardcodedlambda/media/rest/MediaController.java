package com.hardcodedlambda.media.rest;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaRepository mediaRepository;

    @PostMapping("/")
    public ResponseEntity create() {
        Media media = new Media();
        media.setTitle("title");

        mediaRepository.save(media);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public List<Media> getAll() {
        return mediaRepository.findAll();
    }
}
