package com.example.tripai_backend.configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private final String geminiApiKey;

    public AiConfig(@Value("${gemini.api.key}:dummy_key_for_build_purposes") String geminiApiKey) {

        this.geminiApiKey = geminiApiKey;
    }
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey((geminiApiKey))
                .modelName("gemini-2.5-flash")
                .build();
    }
}