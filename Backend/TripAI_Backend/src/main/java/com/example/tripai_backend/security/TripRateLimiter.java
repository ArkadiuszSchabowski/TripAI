package com.example.tripai_backend.security;

import com.example.tripai_backend.exception.InvocationLimitReachedException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TripRateLimiter {
    private static final int MAX_REQUESTS = 10;

    private int count = 0;
    private LocalDate date = LocalDate.now();

    public synchronized void validateInvocationLimit() {

        if (!date.equals(LocalDate.now())) {
            date = LocalDate.now();
            count = 0;
        }

        if (count >= MAX_REQUESTS) {
            throw new InvocationLimitReachedException(
                    "Daily Trip limit reached (" + MAX_REQUESTS + " invocations per day)."
            );
        }
        count++;
    }
}
