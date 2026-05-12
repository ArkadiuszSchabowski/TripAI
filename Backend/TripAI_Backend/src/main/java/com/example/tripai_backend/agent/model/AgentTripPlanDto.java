package com.example.tripai_backend.agent.model;

import dev.langchain4j.model.output.structured.Description;

import java.util.List;

public class AgentTripPlanDto {
    @Description("Engaging introduction that summarizes the trip experience in a short, inspiring way for the user")
    public String tripIntro;

    @Description("Closing message that summarizes the overall experience and leaves a positive final impression of the trip")
    public String closingFeeling;

    @Description("Core trip context including origin and destination cities, travel dates and times, number of travelers and currency used for all calculations")
    public TripInformation information;

    @Description("Estimated trip costs including tickets, accommodation, food, and total cost in the selected currency")
    public  TripCostEstimate costs;

    @Description(
            "Flight details coming strictly from FlightTool output. " +
                    "Use only the selected flight returned by the tool without any modification or inference. " +
                    "If any field is missing, return null."
    )
    public FlightDetails details;

    @Description("Day-by-day travel itinerary including activities and explanations for recommendations. Integrate flight details into Day 1 morning plan.")
    public List<DailyTripPlan> dailyPlan;
}
