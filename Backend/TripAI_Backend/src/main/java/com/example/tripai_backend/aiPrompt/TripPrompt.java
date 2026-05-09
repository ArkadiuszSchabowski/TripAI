package com.example.tripai_backend.aiPrompt;

import com.example.tripai_backend.model.flight.FlightResponseDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TripPrompt {

    public String generateTripPlanPrompt(List<FlightResponseDto> flights, int numberOfPeople) {

        return
                "You are a travel planning assistant.\n\n" +

                        "FLIGHTS:\n" +
                        flights + "\n\n" +

                        "TRIP:\n" +
                        "- People: " + numberOfPeople + "\n\n" +

                        "IMPORTANT INSTRUCTIONS:\n\n" +

                        "RESPONSE STYLE:\n" +
                        "- Be concise.\n" +
                        "- Use bullet points only.\n" +
                        "- Maximum 5–8 bullets per day.\n" +
                        "- No paragraphs.\n" +
                        "- No repetition.\n\n" +

                        "REQUIREMENTS:\n" +
                        "- Create a day-by-day itinerary.\n" +
                        "- Include estimated costs (accommodation, food, activities).\n" +
                        "- Prefer budget-friendly options.\n" +
                        "- Keep suggestions realistic and practical.\n\n" +

                        "OUTPUT FORMAT:\n" +
                        "- Structured daily plan (Day 1, Day 2, etc.)\n" +
                        "- Bullet points only\n";
    }
}
