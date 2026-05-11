package com.example.tripai_backend.agent.model;

public record TripInformation(
        String originCity,
        String destinationCity,
        String departureDateFrom,
        String departureDateTo,
        String numberOfPeople,
        String currency
) {}
