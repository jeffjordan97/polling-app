package com.dizplai.polling.service.controller;

import com.dizplai.polling.service.model.Poll;
import com.dizplai.polling.service.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:4200")
public class PollController {
    @Autowired
    private PollService pollService;

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll){
        Poll createdPoll = pollService.createPoll(poll);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPoll);
    }

    @GetMapping("/updates")
    public SseEmitter getPollUpdates() {
        return pollService.registerEmitter();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable Long id, @RequestBody Poll poll){

        Optional<Poll> existingPoll = pollService.getPollById(id);

        if(existingPoll.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Poll updatedPoll = pollService.updatePoll(id, poll);
        return ResponseEntity.ok(updatedPoll);
    }

    @PutMapping("/active/end")
    public ResponseEntity<Poll> endActivePoll(){
        return pollService.endActivePoll()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<Poll> getAllPolls(){
        return pollService.getAllPolls();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPollById(@PathVariable Long id){
        return pollService.getPollById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Poll> getPollByName(@PathVariable String name){
        return pollService.getPollByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<Poll> getActivePoll(){
        return pollService.getActivePoll()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Poll> deletePollById(@PathVariable Long id){
        return pollService.deletePollById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
