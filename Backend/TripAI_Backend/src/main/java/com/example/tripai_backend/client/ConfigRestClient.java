package com.example.tripai_backend.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
public class ConfigRestClient {

    public org.springframework.web.client.RestClient createClient(String baseUrl){
        return org.springframework.web.client.RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
