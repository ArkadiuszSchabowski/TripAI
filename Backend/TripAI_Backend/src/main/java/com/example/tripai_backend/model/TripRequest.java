package com.example.tripai_backend.model;

import java.time.LocalDate;

public record TripRequest(
        LocalDate fromDate,
        LocalDate toDate,
        String fromCity,
        String toCity,
        int numberOfPeople
) {}