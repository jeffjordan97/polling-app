package com.dizplai.polling.service.controller;

import com.dizplai.polling.service.model.Poll;
import com.dizplai.polling.service.service.PollService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PollControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private PollService pollService;
    @InjectMocks
    private PollController pollController;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pollController).build();
    }

    @Test
    void createPoll_ShouldReturnCreatedPoll() throws Exception {
        Poll mockPoll = new Poll();
        mockPoll.setId(1L);
        mockPoll.setName("TEST POLL");
        mockPoll.setOptions(Arrays.asList("Option 1", "Option 2"));

        when(pollService.createPoll(any(Poll.class))).thenReturn(mockPoll);

        mockMvc.perform(post("/api/polls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sample Poll\",\"options\":[\"Option 1\",\"Option 2\"]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TEST POLL"))
                .andExpect(jsonPath("$.options", hasSize(2)))
                .andExpect(jsonPath("$.options[0]").value("Option 1"))
                .andExpect(jsonPath("$.options[1]").value("Option 2"));

        verify(pollService, times(1)).createPoll(any(Poll.class));
    }

    @Test
    void getPollUpdates_ShouldReturnSseEmitter() throws Exception {
        when(pollService.registerEmitter()).thenReturn(new SseEmitter());

        mockMvc.perform(get("/api/polls/updates"))
                .andExpect(status().isOk());

        verify(pollService, times(1)).registerEmitter();
    }

    @Test
    void updatePoll_ShouldReturnUpdatedPoll() throws Exception {
        Poll mockPoll = new Poll();
        mockPoll.setName("Updated Poll");

        when(pollService.getPollById(1L)).thenReturn(Optional.of(mockPoll));
        when(pollService.updatePoll(eq(1L), any(Poll.class))).thenReturn(mockPoll);

        mockMvc.perform(put("/api/polls/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Poll\",\"options\":[\"Option 1\",\"Option 2\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Poll"));

        verify(pollService, times(1)).updatePoll(eq(1L), any(Poll.class));
    }

    @Test
    void endActivePoll_ShouldReturnEndedPoll() throws Exception {
        Poll mockPoll = new Poll();
        mockPoll.setName("Active Poll");

        when(pollService.endActivePoll()).thenReturn(Optional.of(mockPoll));

        mockMvc.perform(put("/api/polls/active/end"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Active Poll"));

        verify(pollService, times(1)).endActivePoll();
    }

    @Test
    void getAllPolls_ShouldReturnListOfPolls() throws Exception {
        Poll mockPoll1 = new Poll();
        mockPoll1.setName("Poll 1");
        Poll mockPoll2 = new Poll();
        mockPoll2.setName("Poll 1");
        List<Poll> mockPolls = Arrays.asList(mockPoll1,mockPoll2);

        when(pollService.getAllPolls()).thenReturn(mockPolls);

        mockMvc.perform(get("/api/polls/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Poll 1"));

        verify(pollService, times(1)).getAllPolls();
    }

    @Test
    void getPollById_ShouldReturnPoll() throws Exception {
        Poll mockPoll = new Poll();
        mockPoll.setId(1L);
        mockPoll.setName("Poll 1");

        when(pollService.getPollById(1L)).thenReturn(Optional.of(mockPoll));

        mockMvc.perform(get("/api/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Poll 1"));

        verify(pollService, times(1)).getPollById(1L);
    }

    @Test
    void getPollById_ShouldReturnNotFound() throws Exception {
        when(pollService.getPollById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/polls/1"))
                .andExpect(status().isNotFound());

        verify(pollService, times(1)).getPollById(1L);
    }

    @Test
    void deletePollById_ShouldReturnDeletedPoll() throws Exception {
        Poll mockPoll = new Poll();
        mockPoll.setId(1L);
        mockPoll.setName("Poll 1");

        when(pollService.deletePollById(1L)).thenReturn(Optional.of(mockPoll));

        mockMvc.perform(delete("/api/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Poll 1"));

        verify(pollService, times(1)).deletePollById(1L);
    }

}
