package com.hardcodedlambda.media.repository;

import com.hardcodedlambda.media.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Subscriber, Long> {
}
