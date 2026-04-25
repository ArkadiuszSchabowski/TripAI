package com.example.tripai_backend.aiPrompt;

import org.springframework.stereotype.Service;

@Service
public class CityPrompt {
    public String generateIATAPromptForCity(String city){
        return "Return only a 3-letter uppercase IATA airport code for this city: "
                + city
                + ". Output must be exactly 3 letters. No spaces, no newline, no punctuation.";
    }
}
