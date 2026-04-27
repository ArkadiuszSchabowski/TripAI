package com.example.tripai_backend.aiPrompt;

import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.trip.TripRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripPrompt {
    public String generateTripPlanPrompt(List<FlightResponseDto> flights, int numberOfPeople){
        return "Create a travel plan based on the following flights: " + flights +
                ".\n" +
                "IMPORTANT: Respond in Polish language.\n\n" +
                "Requirements:\n" +
                "- The trip is for " + numberOfPeople + " people.\n" +
                "- Provide a day-by-day itinerary for the trip\n" +
                "- Include estimated costs for accommodation, food, and activities\n" +
                "- Prefer cheaper and budget-friendly options\n" +
                "- Keep the plan realistic and practical\n" +
                "- Format the response clearly and structured";
    }

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
