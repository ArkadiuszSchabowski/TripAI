package com.example.tripai_backend.aiPrompt;

import org.springframework.stereotype.Service;

@Service
public class CityPrompt {
    public String getIATACityCodeFromCity(String city){
        return "Zamień nazwę miasta na jego skrót IATACityCode: " + city + "Odpisz tylko trzema literami." + "Przykład: OAT";
    }
}
