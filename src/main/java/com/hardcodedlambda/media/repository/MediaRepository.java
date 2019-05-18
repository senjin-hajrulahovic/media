package com.hardcodedlambda.media.repository;

import com.hardcodedlambda.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {}
