package com.example.tripai_backend.configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private final String geminiApiKey;
    private final String modelName;

    public AiConfig(@Value("${gemini.api.key:test-key}") String geminiApiKey,
                    @Value("${model.name:gemini-2.5-flash}") String modelName) {
        this.geminiApiKey = geminiApiKey;
        this.modelName = modelName;
    }
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey((geminiApiKey))
                .modelName(modelName)
                .build();
    }
}