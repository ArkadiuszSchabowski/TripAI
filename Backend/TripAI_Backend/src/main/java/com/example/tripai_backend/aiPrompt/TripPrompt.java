package com.example.tripai_backend.aiPrompt;

import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.trip.TripRequest;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TripPrompt {

    public String generateTripPlanPrompt(List<FlightResponseDto> flights, TripRequest request) {

        long tripDays = ChronoUnit.DAYS.between(
                request.fromDepartureDate(),
                request.toDepartureDate()
        ) + 1;

        return
                "You are a travel planning assistant.\n\n" +

                        "FLIGHTS:\n" +
                        flights + "\n\n" +

                        "TRIP:\n" +
                        "DATES: FROM " + request.fromDepartureDate() +
                        " TO " + request.toDepartureDate() + "\n" +
                        "CITIES: " + request.originCity() +
                        " -> " + request.destinationCity() + "\n" +
                        "PEOPLE: " + request.numberOfPeople() + "\n" +
                        "DURATION: " + tripDays + " days\n\n" +

                        "IMPORTANT INSTRUCTIONS:\n\n" +

                        "CRITICAL RULES:\n" +
                        "- Each day must match a date within the given range.\n" +
                        "- Do NOT generate more or fewer days than DURATION.\n" +
                        "- Do NOT extend the itinerary beyond the dates.\n\n" +

                        "RESPONSE STYLE:\n" +
                        "- Be concise.\n" +
                        "- Use bullet points only.\n" +
                        "- Maximum 5–8 bullets per day.\n" +
                        "- No paragraphs.\n\n" +

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