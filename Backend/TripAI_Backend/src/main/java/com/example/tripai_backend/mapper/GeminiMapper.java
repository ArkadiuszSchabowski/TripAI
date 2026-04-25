package com.example.tripai_backend.mapper;

import com.example.tripai_backend.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class GeminiMapper {

    private final ObjectMapper objectMapper;

    public GeminiMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getTextFromGeminiJson(String jsonResponse) {
            try {
                JsonNode root = objectMapper.readTree(jsonResponse);

                return root.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText()
                        .trim();

            } catch (JsonProcessingException e) {
                throw new BadRequestException("Invalid Gemini JSON response");
            }
        }
}
