package com.dizplai.polling.service.service;

import com.dizplai.polling.service.exception.PollInvalidException;
import com.dizplai.polling.service.model.Poll;
import com.dizplai.polling.service.model.Vote;
import com.dizplai.polling.service.repository.PollRepository;
import com.dizplai.polling.service.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VoteServiceTest {

    private static final Long TEST_POLL_ID = 1L;
    private static final String TEST_POLL_NAME = "TEST";
    private static final String TEST_POLL_OPTION_1 = "Option 1";
    private static final String TEST_POLL_OPTION_2 = "Option 2";
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private PollRepository pollRepository;
    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addVote_ShouldSaveVote() {
        Vote mockVote = createVote();
        Poll mockPoll = createNewPoll();
        when(pollRepository.findById(1L)).thenReturn(Optional.of(mockPoll));
        when(voteRepository.save(any(Vote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vote result = voteService.addVote(mockVote);

        assertNotNull(result);
        assertEquals(1L, result.getPollId());
        assertEquals("Option 1", result.getOption());
        verify(voteRepository, times(1)).save(mockVote);
    }

    @Test
    void addVote_ShouldThrowExceptionIfPollNotFound() {
        Vote mockVote = new Vote();
        mockVote.setPollId(999L);

        when(pollRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> voteService.addVote(mockVote));
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    void getVotesForPoll_ShouldReturnVotes() {
        List<Vote> mockVotes = Arrays.asList(createVote(), createVote());
        when(voteRepository.findByPollId(1L)).thenReturn(mockVotes);

        List<Vote> result = voteService.getVotesForPoll(1L);

        assertEquals(2, result.size());
        verify(voteRepository, times(1)).findByPollId(1L);
    }

    private Vote createVote(){
        Vote vote = new Vote();
        vote.setPollId(TEST_POLL_ID);
        vote.setOption(TEST_POLL_OPTION_1);
        vote.setTime(LocalDateTime.now());
        return vote;
    }

    private Poll createNewPoll(){
        Poll poll = new Poll();
        poll.setName(TEST_POLL_NAME);
        poll.setOptions(Arrays.asList(TEST_POLL_OPTION_1, TEST_POLL_OPTION_2));
        return poll;
    }
}
