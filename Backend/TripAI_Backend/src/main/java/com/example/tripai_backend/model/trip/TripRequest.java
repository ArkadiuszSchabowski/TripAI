package com.example.tripai_backend.model.trip;

import java.time.LocalDate;

public record TripRequest(
        LocalDate fromDepartureDate,
        LocalDate toDepartureDate,
        String originCity,
        String destinationCity,
        int numberOfPeople
) {}