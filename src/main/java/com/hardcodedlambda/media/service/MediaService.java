package com.hardcodedlambda.media.service;

import com.hardcodedlambda.media.model.Media;
import com.hardcodedlambda.media.model.MediaAssignment;
import com.hardcodedlambda.media.model.MediaAssignmentCreationRequest;
import com.hardcodedlambda.media.model.MediaSearchQuery;
import com.hardcodedlambda.media.repository.MediaAssignmentRepository;
import com.hardcodedlambda.media.elasticsearch.MediaElasticSearchRepository;
import com.hardcodedlambda.media.repository.MediaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
@AllArgsConstructor
@Slf4j
public class MediaService {

    private MediaRepository mediaRepository;
    private MediaAssignmentRepository mediaAssignmentRepository;
    private MediaElasticSearchRepository mediaElasticSearchRepository;

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

    @Transactional
    public List<Media> search(MediaSearchQuery query) {
        return mediaRepository.findAll(query.getSpecification());
    }

    @Transactional
    public List<MediaAssignment> createAssignments(MediaAssignmentCreationRequest request) {

        return request.getMediaAssignmentList().stream()
                .peek(mediaAssignment -> mediaAssignment.setExpiresAt(request.getExpiresAt()))
                .map(mediaAssignmentRepository::save)
                .collect(Collectors.toList());
    }

    public List<MediaAssignment> getAllMediaAssignments() {
        return mediaAssignmentRepository.findAll();
    }

    public List<MediaAssignment> getAllExpiredMediaAssignments(ZonedDateTime zonedDateTime) {
        return mediaAssignmentRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("expiresAt"), zonedDateTime));
    }

    public Iterable<Media> fullTextSearch(String term) {
        return   mediaElasticSearchRepository.search(queryStringQuery(term));
    }
}
