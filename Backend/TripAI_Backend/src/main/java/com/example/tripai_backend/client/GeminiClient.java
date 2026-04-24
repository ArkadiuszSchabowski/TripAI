package com.example.tripai_backend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeminiClient {

        private final String geminiApiKey;
        private final RestClient restClient;

        public GeminiClient(
                ConfigRestClient configRestClient,
                @Value("${google.api.base.url}") String googleApiBaseUrl,
                @Value("${gemini.api.key}") String geminiApiKey) {

            this.geminiApiKey = geminiApiKey;
            this.restClient = configRestClient.createClient(googleApiBaseUrl);
        }

        public String callGeminiApi(Object requestBody){
            return restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/gemma-3-4b-it:generateContent")
                            .queryParam("key", geminiApiKey)
                            .build())
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);
        }
    }