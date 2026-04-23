package com.example.tripai_backend.aiPrompt;

import com.example.tripai_backend.model.Flight.FlightResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripPrompt {
    public String generateTripPlan(List<FlightResponseDto> flights){
        return "Zaplanuj podróż z lotniska: na podstawie:" + flights + "Oszaczuj koszty." + "Daj zwięzłe informacje.";
    }
}
