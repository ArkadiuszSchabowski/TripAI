package com.example.tripai_backend.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    public OpenAiChatModel CreateModel(){
        return OpenAiChatModel.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai/")
                .apiKey("SECRET")
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
