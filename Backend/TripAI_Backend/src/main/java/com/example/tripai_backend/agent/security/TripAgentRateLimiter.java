package com.example.tripai_backend.agent.security;

import com.example.tripai_backend.exception.AgentInvocationLimitReachedException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TripAgentRateLimiter {

    private static final int MAX_REQUESTS = 10;

    private int count = 0;
    private LocalDate date = LocalDate.now();

    public synchronized void validateAgentInvocationLimit() {

        if (!date.equals(LocalDate.now())) {
            date = LocalDate.now();
            count = 0;
        }

        if (count >= MAX_REQUESTS) {
            throw new AgentInvocationLimitReachedException(
                    "Daily Trip Agent limit reached (" + MAX_REQUESTS + " invocations per day)."
            );
        }

        count++;
    }
}