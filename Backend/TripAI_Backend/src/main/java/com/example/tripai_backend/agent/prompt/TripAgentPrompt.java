package com.example.tripai_backend.agent.prompt;

import com.example.tripai_backend.model.trip.TripRequest;
import org.springframework.stereotype.Component;

@Component
public class TripAgentPrompt {
    public String generateTripPlanPromptForAgent(TripRequest request) {
        return "You are an intelligent travel assistant. Your task is to create a travel plan in Polish.\n\n" +
                "TRIP DETAILS:\n" +
                "- From: " + request.originCity() + "\n" +
                "- To: " + request.destinationCity() + "\n" +
                "- Number of people: " + request.numberOfPeople() + "\n" +
                "- Dates: from " + request.fromDepartureDate() + " to " + request.toDepartureDate() + "\n\n" +
                "REQUIREMENTS:\n" +
                "- Provide a day-by-day itinerary.\n" +
                "- Include estimated costs (accommodation, food, activities).\n" +
                "- If the city names are unclear or incorrect, ask the user for clarification.\n\n" +
                "TOOLS:\n" +
                "1. getIataCode - Call this to get the 3-letter IATA code for origin and destination cities.\n" +
                "2. getTopFiveFlights - Call this once you have both IATA codes to find flight connections.";
    }
}
