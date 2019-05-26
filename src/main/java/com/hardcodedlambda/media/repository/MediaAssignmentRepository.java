package com.hardcodedlambda.media.repository;

import com.hardcodedlambda.media.model.MediaAssignment;
import com.hardcodedlambda.media.model.MediaAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MediaAssignmentRepository extends JpaRepository<MediaAssignment, MediaAssignmentId>, JpaSpecificationExecutor<MediaAssignment> {
}

