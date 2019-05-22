package com.hardcodedlambda.media.repository;

import com.hardcodedlambda.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {
}
