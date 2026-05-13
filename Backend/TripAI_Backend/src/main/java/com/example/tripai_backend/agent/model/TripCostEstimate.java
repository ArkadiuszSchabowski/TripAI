package com.example.tripai_backend.agent.model;

import java.math.BigDecimal;

public record TripCostEstimate (
    BigDecimal estimatedTicketCost,
    BigDecimal estimatedAccommodationCost,
    BigDecimal estimatedFoodCost,
    BigDecimal estimatedTotalTripCost
){}

