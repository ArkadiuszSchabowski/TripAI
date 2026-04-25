package com.example.tripai_backend.service;

import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.exception.BadRequestException;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FlyService {

    private final ObjectMapper objectMapper;
    private final DuffelClient duffelClient;

    public FlyService(DuffelClient duffelClient, ObjectMapper objectMapper) {
        this.duffelClient = duffelClient;
        this.objectMapper = objectMapper;
    }

    public String getFlies(GetFlightDto getFlightDto) {
        return duffelClient.getFlights(getFlightDto);
    }

    public List<FlightResponseDto> getTopFlights(List<FlightResponseDto> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(FlightResponseDto::pricePerPerson))
                .limit(5)
                .toList();
    }

    public List<FlightResponseDto> getSimplifiedFlights(String duffelResponse) {

        List<FlightResponseDto> simplifiedFlights = new ArrayList<>();

        try {

            JsonNode root = objectMapper.readTree(duffelResponse);
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
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid Duffel API response");
        }
        return simplifiedFlights;
    }
}
