package com.hardcodedlambda.media;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.repository.MediaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MediaService {

    private MediaRepository mediaRepository;

    public Media create(Media receivedMedia) {
        Media persistedMedia = mediaRepository.save(receivedMedia);
        return persistedMedia;
    }

    public Media get(long id) {
        Media retrievedMedia = mediaRepository.getOne(id);
        return retrievedMedia;
    }

    public Media update(Media receivedMedia) {
        Media persistedMedia = mediaRepository.save(receivedMedia);
        return persistedMedia;
    }

    public void delete(long id) {
        mediaRepository.deleteById(id);
    }

    public List<Media> getAll() {
        return mediaRepository.findAll();
    }

}
