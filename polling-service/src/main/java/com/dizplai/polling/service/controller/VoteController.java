package com.dizplai.polling.service.controller;

import com.dizplai.polling.service.model.Vote;
import com.dizplai.polling.service.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @PostMapping
    public Vote createVote(@RequestBody Vote vote){
        return voteService.addVote(vote);
    }

    @GetMapping("/{pollId}")
    public List<Vote> getVotesForPoll(@PathVariable Long pollId){
        return voteService.getVotesForPoll(pollId);
    }

    @GetMapping("/{pollId}/percentageResults")
    public Map<String, Double> getVotePercentageResultsForPoll(@PathVariable Long pollId) {
        return voteService.getVoteOptionPercentageResultsForPoll(pollId);
    }

    @GetMapping("/updates")
    public SseEmitter getPollVotesUpdates() {
        return voteService.registerEmitter();
    }
}
