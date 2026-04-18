package com.example.tripai_backend.model;

import java.time.LocalDate;
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