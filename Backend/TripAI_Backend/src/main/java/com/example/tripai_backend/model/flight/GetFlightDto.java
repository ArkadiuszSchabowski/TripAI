package com.example.tripai_backend.model.flight;

import java.time.LocalDate;

public record GetFlightDto(
        String originIataCode,
        String destinationIataCode,
        LocalDate fromDepartureDate,
        LocalDate toDepartureDate
) {}