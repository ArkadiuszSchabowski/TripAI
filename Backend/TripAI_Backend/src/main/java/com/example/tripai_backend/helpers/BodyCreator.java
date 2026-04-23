package com.example.tripai_backend.helpers;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BodyCreator {
    public Map<String, Object> CreateBody(String text){
        return Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", text)
                        ))
                ),
                "generationConfig", Map.of(
                        "temperature", 1.0,
                        "maxOutputTokens", 8192,
                        "topP", 0.95,
                        "topK", 40
                )
        );
    }
}
