package com.example.tripai_backend.architecture;

import com.example.tripai_backend.client.GeminiClient;
import com.example.tripai_backend.helpers.GeminiBodyCreator;
import com.example.tripai_backend.mapper.GeminiMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GeminiFacade {
    private final GeminiBodyCreator bodyCreator;
    private final GeminiClient client;
    private final GeminiMapper mapper;

    public GeminiFacade(GeminiBodyCreator bodyCreator, GeminiClient client, GeminiMapper mapper) {
        this.bodyCreator = bodyCreator;
        this.client = client;
        this.mapper = mapper;
    }

    public String generateCityInfo(String prompt){

        Map<String, Object> body = bodyCreator.createBody(prompt);
        String response = client.callApi(body);
        return mapper.getTextFromJson(response);
    }

    public String generateTripPlan(String prompt){

        Map<String, Object> body = bodyCreator.createBody(prompt);
        String response = client.callApi(body);
        return mapper.getTextFromJson(response);
    }
}
