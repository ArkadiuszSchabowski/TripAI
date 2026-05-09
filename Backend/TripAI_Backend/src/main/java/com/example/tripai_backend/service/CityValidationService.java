package com.example.tripai_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Service
public class CityValidationService {

    private final Set<String> cities = new HashSet<>();

    @PostConstruct
    public void load() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream =
                     new ClassPathResource("data/cities.json").getInputStream()) {

            JsonNode root = mapper.readTree(inputStream);

            for (JsonNode country : root) {
                JsonNode states = country.get("states");

                if (states == null) continue;

                for (JsonNode state : states) {
                    JsonNode cityArray = state.get("cities");

                    if (cityArray == null) continue;

                    for (JsonNode cityNode : cityArray) {
                        String name = cityNode.get("name").asText();
                        cities.add(name.toLowerCase());
                    }
                }
            }
        }
    }

    public boolean exists(String city) {
        if (city == null) return false;
        return cities.contains(city.toLowerCase());
    }
}