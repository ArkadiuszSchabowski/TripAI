package com.example.tripai_backend.service;

import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.model.Flight.FlightResponseDto;
import com.example.tripai_backend.model.Flight.GetFlightDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlyService {

    private final DuffelClient duffelClient;

    public FlyService(DuffelClient duffelClient) {
        this.duffelClient = duffelClient;
    }

    public String getFlies(GetFlightDto getFlightDto) {
        return duffelClient.getFlights(getFlightDto);
    }

    @SneakyThrows
    public List<FlightResponseDto> getSimplifiedFlights(String duffelResponse) {

        ObjectMapper mapper = new ObjectMapper();
        List<FlightResponseDto> simplifiedFlights = new ArrayList<>();

        JsonNode root = mapper.readTree(duffelResponse);
        JsonNode offers = root.path("data").path("offers");

        for (JsonNode offer : offers) {
            JsonNode slices = offer.path("slices");

            JsonNode sliceTam = slices.get(0);
            String origin = sliceTam.path("origin").path("name").asText();
            String destination = sliceTam.path("destination").path("name").asText();
            String dateTam = sliceTam.path("segments").get(0).path("departing_at").asText();

            String dateBack = (slices.size() > 1)
                    ? slices.get(1).path("segments").get(0).path("departing_at").asText()
                    : "One way only";

            double totalAmount = offer.path("total_amount").asDouble();
            int passengerCount = offer.path("passengers").size();
            double pricePerPerson = totalAmount / (passengerCount > 0 ? passengerCount : 1);

            String airline = offer.path("owner").path("name").asText();

            simplifiedFlights.add(new FlightResponseDto(
                    origin,
                    destination,
                    dateTam,
                    dateBack,
                    airline,
                    pricePerPerson
            ));
        }
        return simplifiedFlights;
    }
}
