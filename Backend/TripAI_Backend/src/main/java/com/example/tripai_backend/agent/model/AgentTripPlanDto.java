package com.example.tripai_backend.agent.model;

import dev.langchain4j.model.output.structured.Description;

import java.util.List;

public class AgentTripPlanDto {
    @Description("Detailed travel information including cities and dates")
    public TripInformation information;

    @Description("A list of daily plans for the trip")
    public List<DailyTripPlan> dailyPlan;
}
