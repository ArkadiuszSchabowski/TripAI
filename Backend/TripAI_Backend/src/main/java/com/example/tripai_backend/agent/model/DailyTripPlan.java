package com.example.tripai_backend.agent.model;

public record DailyTripPlan (
        int dayNumber,
        String morningPlan,
        String morningWhy,
        String afternoonPlan,
        String afternoonWhy,
        String tonightPlan,
        String nightWhy
) {}