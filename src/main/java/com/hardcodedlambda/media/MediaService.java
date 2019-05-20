package com.hardcodedlambda.media;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.repository.MediaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class MediaService {

    private MediaRepository mediaRepository;

    public Media create(Media receivedMedia) {
        receivedMedia.setCreatedAt(ZonedDateTime.now());
        receivedMedia.setUpdatedLastAt(ZonedDateTime.now());
        return  mediaRepository.save(receivedMedia);
    }

    public Optional<Media> get(Long id) {
        try {
            return mediaRepository.findById(id);
        } catch (Exception e) {
            log.error("Exception happened during attempt to get Media with id {}", id, e);
            return Optional.empty();
        }
    }

    public Media update(Media receivedMedia) {
        Media existingMedia = mediaRepository.getOne(receivedMedia.getId());
        receivedMedia.setCreatedAt(existingMedia.getCreatedAt());
        receivedMedia.setUpdatedLastAt(ZonedDateTime.now());
        return mediaRepository.save(receivedMedia);
    }

    public boolean delete(Long id) {
        try {
            Optional<Media> existingMedia = mediaRepository.findById(id);
            mediaRepository.deleteById(id);
            return existingMedia.isPresent();
        } catch (Exception e) {
            // TODO - re-implement in a better way
            return false;
        }
    }

    public List<Media> getAll() {
        return mediaRepository.findAll();
    }

}
