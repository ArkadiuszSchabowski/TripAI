package com.example.tripai_backend.service;

import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.client.ConfigRestClient;
import com.example.tripai_backend.client.GeminiClient;
import com.example.tripai_backend.helpers.BodyCreator;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.example.tripai_backend.model.trip.TripRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    private final GeminiClient geminiClient;
    private final FlyService flyService;
    private final CityPrompt cityPromptService;
    private final TripPrompt tripPromptService;
    private  final BodyCreator bodyCreator;

    public TripService(BodyCreator bodyCreator, CityPrompt cityPromptService, GeminiClient geminiClient, TripPrompt tripPromptService, FlyService flyService) {
        this.bodyCreator = bodyCreator;
        this.cityPromptService = cityPromptService;
        this.geminiClient = geminiClient;
        this.flyService = flyService;
        this.tripPromptService = tripPromptService;
    }

    @SneakyThrows
    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.destinationCity()).trim();

        var originCityRequestBody = bodyCreator.CreateBody(originCityPrompt);

        var destinationCityRequestBody = bodyCreator.CreateBody(destinationCityPrompt);

        String originCityResponse = geminiClient.callGeminiApi(originCityRequestBody);

        String destinationCityResponse = geminiClient.callGeminiApi(destinationCityRequestBody);

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

        var topFlights = flyService.getTopFlights(simplifiedFlight);

        String tripPrompt = tripPromptService.generateTripPlan(topFlights, tripRequest.numberOfPeople());

        var tripRequestBody = bodyCreator.CreateBody(tripPrompt);

        var tripResponse = geminiClient.callGeminiApi(tripRequestBody);

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