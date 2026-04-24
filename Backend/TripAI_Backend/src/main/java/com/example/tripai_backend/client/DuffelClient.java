package com.example.tripai_backend.client;

import com.example.tripai_backend.model.duffel.DuffelOfferRequest;
import com.example.tripai_backend.model.duffel.Passenger;
import com.example.tripai_backend.model.duffel.Slice;
import com.example.tripai_backend.model.flight.GetFlightDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class DuffelClient {

    private final RestTemplate restTemplate;

    @Value("${duffel.api.key}")
    private String duffelApiKey;

    @Value("${duffel.api.base.url}")
    private String duffelApiBaseUrl;

    public DuffelClient() {
        this.restTemplate = new RestTemplate();
    }

    public String getFlights(GetFlightDto dto) {

        Slice departureSlice = new Slice(
                dto.IATACityCodeOriginCity(),
                dto.IATACityCodeDestinationCity(),
                dto.fromDepartureDate().toString()
        );

        Slice returnSlice = new Slice(
                dto.IATACityCodeDestinationCity(),
                dto.IATACityCodeOriginCity(),
                dto.toDepartureDate().toString()
        );

        DuffelOfferRequest offerRequest = new DuffelOfferRequest(
                List.of(departureSlice, returnSlice),
                List.of(new Passenger("adult")),
                "economy"
        );

        Map<String, Object> wrappedBody = Map.of("data", offerRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + duffelApiKey);
        headers.set("Duffel-Version", "v2");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(wrappedBody, headers);

        return restTemplate.postForObject(duffelApiBaseUrl, entity, String.class);
    }
}