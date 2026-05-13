package com.example.tripai_backend.agent.model;

import java.time.LocalDate;

public record TripInformation(
        String originCity,
        String destinationCity,
        LocalDate departureDateFrom,
        LocalDate departureDateTo,
        int numberOfPeople,
        String currency
) {}
