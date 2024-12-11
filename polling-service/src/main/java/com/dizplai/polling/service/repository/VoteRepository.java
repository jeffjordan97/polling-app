package com.dizplai.polling.service.repository;

import com.dizplai.polling.service.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByPollId(Long pollId);
}
