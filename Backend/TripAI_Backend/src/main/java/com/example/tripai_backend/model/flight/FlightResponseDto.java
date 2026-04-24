package com.example.tripai_backend.model.flight;

public record FlightResponseDto(
            String originCity,
            String destinationCity,
            String departureDateTam,
            String departureDateBack,
            String airlineName,
            Double pricePerPerson
) {}

