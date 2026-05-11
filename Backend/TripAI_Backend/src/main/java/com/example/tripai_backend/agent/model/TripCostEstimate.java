package com.example.tripai_backend.agent.model;

public record TripCostEstimate (
    String estimatedTicketCost,
    String estimatedAccommodationCost,
    String estimatedFoodCost,
    String estimatedTotalTripCost
){}

