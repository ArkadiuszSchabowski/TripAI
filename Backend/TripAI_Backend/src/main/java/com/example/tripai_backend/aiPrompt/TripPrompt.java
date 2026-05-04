package com.example.tripai_backend.aiPrompt;

import com.example.tripai_backend.model.flight.FlightResponseDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
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
}
