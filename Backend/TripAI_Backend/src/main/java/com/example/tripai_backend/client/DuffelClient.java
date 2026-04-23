package com.example.tripai_backend.client;

import com.example.tripai_backend.model.Flight.GetFlightDto;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public DuffelClient() {
        this.restTemplate = new RestTemplate();
    }

    public String getFlights(GetFlightDto dto) {
        String url = "https://api.duffel.com/air/offer_requests";

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
        headers.set("Authorization", "Bearer X");
        headers.set("Duffel-Version", "v2");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(wrappedBody, headers);

        return restTemplate.postForObject(url, entity, String.class);
    }

    private record DuffelOfferRequest(
            @JsonProperty("slices") List<Slice> slices,
            @JsonProperty("passengers") List<Passenger> passengers,
            @JsonProperty("cabin_class") String cabinClass
    ) {}

    private record Slice(
            @JsonProperty("origin") String origin,
            @JsonProperty("destination") String destination,
            @JsonProperty("departure_date") String departureDate
    ) {}

    private record Passenger(
            @JsonProperty("type") String type
    ) {}
}