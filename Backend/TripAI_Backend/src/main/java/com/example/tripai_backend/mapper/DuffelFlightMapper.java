package com.example.tripai_backend.mapper;

import com.example.tripai_backend.exception.BadRequestException;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DuffelFlightMapper {

    private final ObjectMapper objectMapper;

    public DuffelFlightMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<FlightResponseDto> mapToFlights(String json){

        List<FlightResponseDto> listFlights = new ArrayList<>();

        try {

            JsonNode root = objectMapper.readTree(json);
            JsonNode offers = root.path("data").path("offers");

            for (JsonNode offer : offers) {
                JsonNode slices = offer.path("slices");

                JsonNode departureTrip = slices.get(0);
                String origin = departureTrip.path("origin").path("name").asText();
                String destination = departureTrip.path("destination").path("name").asText();
                String departureDate = departureTrip.path("segments").get(0).path("departing_at").asText();

                String returnDate = (slices.size() > 1)
                        ? slices.get(1).path("segments").get(0).path("departing_at").asText()
                        : "One way only";

                double totalAmount = offer.path("total_amount").asDouble();
                int passengerCount = offer.path("passengers").size();
                double pricePerPerson = totalAmount / (passengerCount > 0 ? passengerCount : 1);

                String airline = offer.path("owner").path("name").asText();


                listFlights.add(new FlightResponseDto(
                        origin,
                        destination,
                        departureDate,
                        returnDate,
                        airline,
                        pricePerPerson
                ));
            }
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid Duffel API response");
        }
        return listFlights;
    }
}
