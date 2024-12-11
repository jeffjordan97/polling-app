package com.dizplai.polling.service.repository;

import com.dizplai.polling.service.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Optional<Poll> findFirstByEndDateIsNull();
    Optional<Poll> findFirstByName(String name);
}
