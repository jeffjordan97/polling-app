package com.dizplai.polling.service.validator;

import com.dizplai.polling.service.exception.PollInvalidException;
import com.dizplai.polling.service.model.Poll;
import com.dizplai.polling.service.repository.PollRepository;
import com.dizplai.polling.service.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PollValidator {
    @Autowired
    private PollRepository pollRepository;
    public void validatePoll(Poll poll) {
        if (poll == null) {
            throw new PollInvalidException("Poll cannot be null.");
        }

        // Validate poll name
        if (poll.getName() == null || poll.getName().trim().isEmpty()) {
            throw new PollInvalidException("Poll name must not be empty.");
        }

        // Validate options
        if (poll.getOptions() == null || poll.getOptions().size() < 2 || poll.getOptions().size() > 7) {
            throw new PollInvalidException("Poll must have between 2 and 7 options.");
        }
    }
}
