package com.dizplai.polling.service.controller;

import com.dizplai.polling.service.model.Vote;
import com.dizplai.polling.service.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VoteControllerTest {

    @Mock
    private VoteService voteService;
    @InjectMocks
    private VoteController voteController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(voteController).build();
    }

    @Test
    void createVote_ShouldReturnCreatedVote() throws Exception {
        Vote mockVote = new Vote();
        mockVote.setPollId(101L);
        mockVote.setOption("Option1");

        when(voteService.addVote(any(Vote.class))).thenReturn(mockVote);

        mockMvc.perform(post("/api/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pollId\":101,\"option\":\"Option1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pollId").value(101))
                .andExpect(jsonPath("$.option").value("Option1"));

        verify(voteService, times(1)).addVote(any(Vote.class));
    }

    @Test
    void getVotesForPoll_ShouldReturnListOfVotes() throws Exception {
        Vote mockVote1 = new Vote();
        mockVote1.setPollId(101L);
        mockVote1.setOption("Option1");
        Vote mockVote2 = new Vote();
        mockVote2.setPollId(101L);
        mockVote2.setOption("Option2");
        List<Vote> mockVotes = Arrays.asList(mockVote1,mockVote2);

        when(voteService.getVotesForPoll(101L)).thenReturn(mockVotes);

        mockMvc.perform(get("/api/votes/101")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].option").value("Option1"))
                .andExpect(jsonPath("$[1].option").value("Option2"));

        verify(voteService, times(1)).getVotesForPoll(101L);
    }

    @Test
    void getVotePercentageResultsForPoll_ShouldReturnVotePercentages() throws Exception {
        Map<String, Double> mockResults = Map.of("Option1", 60.0, "Option2", 40.0);

        when(voteService.getVoteOptionPercentageResultsForPoll(101L)).thenReturn(mockResults);

        mockMvc.perform(get("/api/votes/101/percentageResults")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Option1").value(60.0))
                .andExpect(jsonPath("$.Option2").value(40.0));

        verify(voteService, times(1)).getVoteOptionPercentageResultsForPoll(101L);
    }

    @Test
    void getPollVotesUpdates_ShouldReturnSseEmitter() throws Exception {
        when(voteService.registerEmitter()).thenReturn(new SseEmitter());

        mockMvc.perform(get("/api/votes/updates"))
                .andExpect(status().isOk());

        verify(voteService, times(1)).registerEmitter();
    }
}
