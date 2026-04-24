package com.example.tripai_backend.model.trip;

import java.time.LocalDateTime;

public record TripResponse(
        int durationDays,
        int totalTicketsCost,
        LocalDateTime outboundDepartureTime,
        LocalDateTime outboundArrivalDate,
        LocalDateTime returnDepartureTime,
        LocalDateTime returnArrivalDate,
        String tripPlan
) {}