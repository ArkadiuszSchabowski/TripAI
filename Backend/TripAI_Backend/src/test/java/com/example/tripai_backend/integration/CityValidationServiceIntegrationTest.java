package com.example.tripai_backend.integration;

import com.example.tripai_backend.service.CityValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CityValidationServiceIntegrationTest {
    @Autowired
    CityValidationService service;

    @ParameterizedTest
    @CsvSource({
            "London, true",
            "LONDIN, false",
            "Paris, true",
            "Pari, false",
            "Madrid, true",
            "Madrit, false",
            "TOKYO, true",
            "TOKIO, false",
            "BARCELONA, true",
            "nonexistentcity, false",
    })
    void exists_whenCalled_shouldValidateCities(String city, boolean expected) {
        assertEquals(expected, service.exists(city));
    }
}
