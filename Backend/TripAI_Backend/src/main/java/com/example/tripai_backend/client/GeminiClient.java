package com.example.tripai_backend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeminiClient {

        private final String geminiApiKey;
        private final RestClient restClient;
        private final String modelName;

        public GeminiClient(
                ConfigRestClient configRestClient,
                @Value("${gemini.api.base.url}") String googleApiBaseUrl,
                @Value("${gemini.api.key}") String geminiApiKey,
                @Value("${model.name}") String modelName) {
            this.modelName = modelName;
            this.geminiApiKey = geminiApiKey;
            this.restClient = configRestClient.createClient(googleApiBaseUrl);
        }

        public String callApi(Object requestBody){
            return restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/{modelName}:generateContent")
                            .queryParam("key", geminiApiKey)
                            .build(modelName))
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);
        }
    }