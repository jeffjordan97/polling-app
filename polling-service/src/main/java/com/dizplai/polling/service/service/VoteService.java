package com.dizplai.polling.service.service;

import com.dizplai.polling.service.model.Poll;
import com.dizplai.polling.service.model.Vote;
import com.dizplai.polling.service.repository.PollRepository;
import com.dizplai.polling.service.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private PollRepository pollRepository;
    private final ArrayList<SseEmitter> emitters = new ArrayList<>();

    public Vote addVote(Vote vote){
        Long pollId = vote.getPollId();
        getPollIfPollIdIsValid(pollId);
        vote.setTime(LocalDateTime.now());

        return voteRepository.save(vote);
    }

    public List<Vote> getVotesForPoll(Long pollId){
        return voteRepository.findByPollId(pollId);
    }

    public Poll getPollIfPollIdIsValid(Long pollId){
        return pollRepository.findById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Poll with ID "+pollId+" not found."));
    }

    public Map<String, Double> getVoteOptionPercentageResultsForPoll(Long pollId){
        Poll poll = getPollIfPollIdIsValid(pollId);
        List<Vote> votes = getVotesForPoll(pollId);
        Map<String, Double> voteOptionPercentages = calculateVoteOptionPercentages(votes, poll.getOptions());
        notifyClients(voteOptionPercentages);
        return voteOptionPercentages;
    }

    private Map<String, Double> calculateVoteOptionPercentages(List<Vote> votes, List<String> options) {
        long totalVotes = votes.size();
        if (totalVotes == 0) {
            return options.stream().collect(Collectors.toMap(option -> option, option -> 0.0));
        }

        Map<String, Long> voteCounts = votes.stream()
                .collect(Collectors.groupingBy(Vote::getOption, Collectors.counting()));

        return options.stream()
                .collect(Collectors.toMap(
                        option -> option,
                        option -> (voteCounts.getOrDefault(option, 0L) * 100.0) / totalVotes
                ));
    }

    public SseEmitter registerEmitter() {
        return addEmitter();
    }

    public SseEmitter addEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    public void notifyClients(Map<String, Double> data) {
        Iterator<SseEmitter> iterator = emitters.iterator();
        while (iterator.hasNext()) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(data);
            } catch (IOException e) {
                iterator.remove();
            }
        }
    }
}
