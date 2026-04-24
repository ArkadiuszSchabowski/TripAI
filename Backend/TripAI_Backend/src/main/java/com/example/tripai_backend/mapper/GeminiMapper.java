package com.example.tripai_backend.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class GeminiMapper {

    private final ObjectMapper objectMapper;

    public GeminiMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public String getTextFromGeminiJson(String jsonResponse) {
            JsonNode root = objectMapper.readTree(jsonResponse);
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText()
                    .trim();
    }
}
