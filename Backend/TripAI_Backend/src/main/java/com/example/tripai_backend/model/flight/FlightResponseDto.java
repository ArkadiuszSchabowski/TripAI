package com.example.tripai_backend.model.flight;

public record FlightResponseDto(
            String originCity,
            String destinationCity,
            String fromDepartureDate,
            String toDepartureDate,
            String airlineName,
            Double pricePerPerson
) {}

