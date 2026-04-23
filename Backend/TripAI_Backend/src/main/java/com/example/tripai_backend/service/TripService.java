package com.example.tripai_backend.service;

import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.model.Flight.FlightResponseDto;
import com.example.tripai_backend.model.Flight.GetFlightDto;
import com.example.tripai_backend.model.Trip.TripRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class TripService {

    private final FlyService flyService;
    private final CityPrompt cityPrompt;

    public TripService(CityPrompt cityPrompt, FlyService flyService)
    {
        this.cityPrompt = cityPrompt;
        this.flyService = flyService;
    }

    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPrompt.getIATACityCodeFromCity(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPrompt.getIATACityCodeFromCity(tripRequest.destinationCity()).trim();


        String apiKey = "X";
        String baseUrl = "https://generativelanguage.googleapis.com";

        var originCityRequestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", originCityPrompt)
                        ))
                ),
                "generationConfig", Map.of(
                        "temperature", 1.0,
                        "maxOutputTokens", 8192,
                        "topP", 0.95,
                        "topK", 40
                )
        );;

        var destinationCityRequestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", destinationCityPrompt)
                        ))
                ),
                "generationConfig", Map.of(
                        "temperature", 1.0,
                        "maxOutputTokens", 8192,
                        "topP", 0.95,
                        "topK", 40
                )
        );;

        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

//        String originCityResponse = restClient.post()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
//                        .queryParam("key", apiKey)
//                        .build())
//                .body(originCityRequestBody)
//                .retrieve()
//                .body(String.class);
//
//        String destinationCityResponse = restClient.post()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
//                        .queryParam("key", apiKey)
//                        .build())
//                .body(destinationCityRequestBody)
//                .retrieve()
//                .body(String.class);

//                originCityResponse,
//                destinationCityResponse,
        var getFlightDto = new GetFlightDto(
                "WAW",
                "BCN",
                tripRequest.fromDepartureDate(),
                tripRequest.toDepartureDate()
        );

        //Pobierz loty
        var duffelResponse = flyService.getFlies(getFlightDto);

        var simplifiedFlight = getSimplifiedFlights(duffelResponse);

        var topFlights = simplifiedFlight.stream()
                .sorted(Comparator.comparing(FlightResponseDto::pricePerPerson))
                .limit(5)
                .toList();

        String tripPrompt = "Zaplanuj podróż z lotniska: na podstawie:" + topFlights + "Oszaczuj koszty." + "Daj zwięzłe informacje.";

        var tripRequestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", tripPrompt)
                        ))
                ),
                "generationConfig", Map.of(
                        "temperature", 1.0,
                        "maxOutputTokens", 8192,
                        "topP", 0.95,
                        "topK", 40
                )
        );;

                String tripResponse = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .body(tripRequestBody)
                .retrieve()
                .body(String.class);

        return tripResponse;
    }

    @SneakyThrows
    public List<FlightResponseDto> getSimplifiedFlights(String duffelResponse) {
        String jsonResponse = duffelResponse;

        ObjectMapper mapper = new ObjectMapper();
        List<FlightResponseDto> simplifiedFlights = new ArrayList<>();

        JsonNode root = mapper.readTree(jsonResponse);
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
