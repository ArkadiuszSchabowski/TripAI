package com.example.tripai_backend.agent.tool;

import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.client.GeminiClient;
import com.example.tripai_backend.helpers.GeminiBodyCreator;
import com.example.tripai_backend.mapper.GeminiMapper;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IataTool {
    private final CityPrompt cityPromptService;
    private final GeminiBodyCreator bodyCreator;
    private final GeminiClient client;
    private final GeminiMapper mapper;

    public IataTool(CityPrompt cityPromptService, GeminiBodyCreator bodyCreator, GeminiClient client, GeminiMapper mapper) {
        this.cityPromptService = cityPromptService;
        this.bodyCreator = bodyCreator;
        this.client = client;
        this.mapper = mapper;
    }

    @Tool("Returns IATA airport code for a city")
    public String getIataCode(String city){

        String prompt = cityPromptService.generateIataPromptForCity(city);

        Map<String, Object> body = bodyCreator.createBody(prompt);
        String response = client.callApi(body);
        return mapper.getTextFromJson(response);
    }
}
