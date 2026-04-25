package com.example.tripai_backend.architecture;

import com.example.tripai_backend.client.GeminiClient;
import com.example.tripai_backend.helpers.GeminiBodyCreator;
import com.example.tripai_backend.mapper.GeminiMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Gemini {
    private final GeminiBodyCreator bodyCreator;
    private final GeminiClient client;
    private final GeminiMapper mapper;

    public Gemini(GeminiBodyCreator bodyCreator, GeminiClient client, GeminiMapper mapper) {
        this.bodyCreator = bodyCreator;
        this.client = client;
        this.mapper = mapper;
    }
    public Map<String, Object> createBody(String text){
        return bodyCreator.createBody(text);
    }

    public String callApi(Object requestBody){
        return client.callGeminiApi(requestBody);
    }

    public String getTextFromJson(String jsonResponse){
        return mapper.getTextFromGeminiJson(jsonResponse);
    }
}
