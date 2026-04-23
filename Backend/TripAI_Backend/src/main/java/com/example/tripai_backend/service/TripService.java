package com.example.tripai_backend.service;

import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.client.ConfigRestClient;
import com.example.tripai_backend.helpers.BodyCreator;
import com.example.tripai_backend.model.Flight.FlightResponseDto;
import com.example.tripai_backend.model.Flight.GetFlightDto;
import com.example.tripai_backend.model.Trip.TripRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Comparator;

@Service
public class TripService {

    private final FlyService flyService;
    private final CityPrompt cityPromptService;
    private final TripPrompt tripPromptService;
    private  final BodyCreator bodyCreator;

    public TripService(BodyCreator bodyCreator, CityPrompt cityPromptService, TripPrompt tripPromptService, FlyService flyService) {
        this.bodyCreator = bodyCreator;
        this.cityPromptService = cityPromptService;
        this.flyService = flyService;
        this.tripPromptService = tripPromptService;
    }

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${google.api.base.url}")
    private String googleApiBaseUrl;

    @SneakyThrows
    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.destinationCity()).trim();

        var configRestClient = new ConfigRestClient();
        RestClient restClient = configRestClient.createClient(googleApiBaseUrl);

        var originCityRequestBody = bodyCreator.CreateBody(originCityPrompt);

        var destinationCityRequestBody = bodyCreator.CreateBody(destinationCityPrompt);

        String originCityResponse = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
                        .queryParam("key", geminiApiKey)
                        .build())
                .body(originCityRequestBody)
                .retrieve()
                .body(String.class);

        String destinationCityResponse = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
                        .queryParam("key", geminiApiKey)
                        .build())
                .body(destinationCityRequestBody)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootOriginCity = mapper.readTree(originCityResponse);

        var originCity = rootOriginCity
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText()
                .trim();

        JsonNode rootDestinationCity = mapper.readTree(destinationCityResponse);

        var destinationCity = rootDestinationCity
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText()
                .trim();

        var getFlightDto = new GetFlightDto(
                originCity,
                destinationCity,
                tripRequest.fromDepartureDate(),
                tripRequest.toDepartureDate()
        );

        var duffelResponse = flyService.getFlies(getFlightDto);

        var simplifiedFlight = flyService.getSimplifiedFlights(duffelResponse);

        var topFlights = simplifiedFlight.stream()
                .sorted(Comparator.comparing(FlightResponseDto::pricePerPerson))
                .limit(5)
                .toList();


        String tripPrompt = tripPromptService.generateTripPlan(topFlights);

        var tripRequestBody = bodyCreator.CreateBody(tripPrompt);

        var tripResponse = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
                        .queryParam("key", geminiApiKey)
                        .build())
                .body(tripRequestBody)
                .retrieve()
                .body(String.class);

        JsonNode rootTrip = mapper.readTree(tripResponse);

        var tripPlan = rootTrip
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText()
                .trim();

        return tripPlan;
    }
}