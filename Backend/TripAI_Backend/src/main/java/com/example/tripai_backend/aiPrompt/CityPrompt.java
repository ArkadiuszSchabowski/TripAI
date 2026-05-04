package com.example.tripai_backend.aiPrompt;

import org.springframework.stereotype.Component;

@Component
public class CityPrompt {
    public String generateIataPromptForCity(String city){
        return "Return only a 3-letter uppercase IATA airport code for this city: "
                + city
                + ". Output must be exactly 3 letters. No spaces, no newline, no punctuation."
                .trim();
    }
}
