package com.dizplai.polling.service.service;

import com.dizplai.polling.service.exception.PollInvalidException;
import com.dizplai.polling.service.model.Poll;
import com.dizplai.polling.service.repository.PollRepository;
import com.dizplai.polling.service.validator.PollValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PollServiceTest {
    private static final String TEST_POLL_NAME = "TEST";
    private static final String TEST_POLL_OPTION_1 = "Option 1";
    private static final String TEST_POLL_OPTION_2 = "Option 2";
    @Mock
    private PollRepository pollRepository;
    @Mock
    private PollValidator pollValidator;
    @InjectMocks
    private PollService pollService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPoll_ShouldSaveAndNotifyClients() {
        Poll mockPoll = createNewPoll();
        when(pollRepository.save(any(Poll.class))).thenReturn(mockPoll);

        Poll result = pollService.createPoll(mockPoll);

        assertNotNull(result);
        assertEquals(TEST_POLL_NAME, result.getName());
        verify(pollValidator, times(1)).validatePoll(mockPoll);
        verify(pollRepository, times(1)).save(mockPoll);
    }

    @Test
    void getAllPolls_ShouldReturnAllPolls() {
        List<Poll> mockPolls = Arrays.asList(createNewPoll(),createNewPoll());
        when(pollRepository.findAll()).thenReturn(mockPolls);

        List<Poll> result = pollService.getAllPolls();

        assertEquals(2, result.size());
        verify(pollRepository, times(1)).findAll();
    }

    @Test
    void getPollById_ShouldReturnPoll() {
        Poll mockPoll = createNewPoll();
        when(pollRepository.findById(1L)).thenReturn(Optional.of(mockPoll));

        Optional<Poll> result = pollService.getPollById(1L);

        assertTrue(result.isPresent());
        assertEquals(TEST_POLL_NAME, result.get().getName());
        verify(pollRepository, times(1)).findById(1L);
    }

    @Test
    void getActivePoll_ShouldReturnActivePoll() {
        Poll mockPoll = createNewPoll();
        when(pollRepository.findFirstByEndDateIsNull()).thenReturn(Optional.of(mockPoll));

        Optional<Poll> result = pollService.getActivePoll();

        assertTrue(result.isPresent());
        assertEquals(TEST_POLL_NAME, result.get().getName());
        verify(pollRepository, times(1)).findFirstByEndDateIsNull();
    }

    @Test
    void updatePoll_ShouldThrowExceptionIfPollNotFound() {
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        Poll updatedPoll = createNewPoll();
        updatedPoll.setOptions(List.of(TEST_POLL_OPTION_1));

        assertThrows(PollInvalidException.class, () -> pollService.updatePoll(1L, updatedPoll));
        verify(pollRepository, times(1)).findById(1L);
        verify(pollRepository, never()).save(any(Poll.class));
    }

    private Poll createNewPoll(){
        Poll poll = new Poll();
        poll.setName(TEST_POLL_NAME);
        poll.setOptions(Arrays.asList(TEST_POLL_OPTION_1, TEST_POLL_OPTION_2));
        return poll;
    }
}
