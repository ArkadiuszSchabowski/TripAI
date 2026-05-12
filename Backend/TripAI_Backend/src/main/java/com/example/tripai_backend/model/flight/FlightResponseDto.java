package com.example.tripai_backend.model.flight;

public record FlightResponseDto(
            String originCity,
            String destinationCity,
            String outboundDeparture,
            String outboundArrival,
            String returnDeparture,
            String returnArrival,
            String airlineName,
            Double pricePerPerson
) {}

