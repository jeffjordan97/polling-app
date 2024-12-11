package com.dizplai.polling.service.service;

import com.dizplai.polling.service.exception.PollInvalidException;
import com.dizplai.polling.service.model.Poll;
import com.dizplai.polling.service.repository.PollRepository;
import com.dizplai.polling.service.validator.PollValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private PollValidator pollValidator;
    private final ArrayList<SseEmitter> emitters = new ArrayList<>();

    public Poll createPoll(Poll poll){
        pollValidator.validatePoll(poll);
        poll.setStartDate(LocalDateTime.now());

        endActivePoll();

        Poll savedPoll = pollRepository.save(poll);

        notifyClients(savedPoll);

        return savedPoll;
    }

    public List<Poll> getAllPolls(){
        return pollRepository.findAll();
    }

    public Optional<Poll> getPollById(Long id){
        return pollRepository.findById(id);
    }

    public Optional<Poll> getPollByName(String name){
        return pollRepository.findFirstByName(name);
    }

    public Optional<Poll> getActivePoll(){
        return pollRepository.findFirstByEndDateIsNull();
    }

    public Poll updatePoll(Long id, Poll updatedPoll) {
        Poll existingPoll = pollRepository.findById(id)
                .orElseThrow(() -> new PollInvalidException("Poll with ID " + id + " not found."));

        pollValidator.validatePoll(updatedPoll);

        existingPoll.setName(updatedPoll.getName());
        existingPoll.setOptions(updatedPoll.getOptions());

        return pollRepository.save(existingPoll);
    }

    public Optional<Poll> deletePollById(Long id){
        Optional<Poll> pollToDelete = getPollById(id);
        if(pollToDelete.isPresent()){
            pollRepository.deleteById(id);
        }
        return pollToDelete;
    }

    public Optional<Poll> endActivePoll() {
        Optional<Poll> activePoll = pollRepository.findFirstByEndDateIsNull();
        activePoll.ifPresent(poll -> {
            // Set the end date of the current poll to current date time
            poll.setEndDate(LocalDateTime.now());
            pollRepository.save(poll);
        });
        return activePoll;
    }

    public SseEmitter registerEmitter() {
        System.out.println("RegisterEmitter called.");
        return addEmitter();
    }
    // TODO Refactor into SseEmitterUtil class to reuse the same logic in VoteService
    public SseEmitter addEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    public void notifyClients(Poll data) {
        List<SseEmitter> emittersToRemove = new ArrayList<>();
        Iterator<SseEmitter> iterator = emitters.iterator();
        while (iterator.hasNext()) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(data);
            } catch (IOException e) {
                iterator.remove(); // Safely remove the emitter
            }
        }
    }

}
