package com.example.tripai_backend.service;

import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.client.ConfigRestClient;
import com.example.tripai_backend.model.Flight.FlightResponseDto;
import com.example.tripai_backend.model.Flight.GetFlightDto;
import com.example.tripai_backend.model.Trip.TripRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class TripService {

    private final FlyService flyService;
    private final CityPrompt cityPromptService;
    private final TripPrompt tripPromptService;

    public TripService(CityPrompt cityPromptService, TripPrompt tripPromptService, FlyService flyService) {
        this.cityPromptService = cityPromptService;
        this.flyService = flyService;
        this.tripPromptService = tripPromptService;
    }

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${google.api.base.url}")
    private String googleApiBaseUrl;

    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.destinationCity()).trim();

        var configRestClient = new ConfigRestClient();
        RestClient restClient = configRestClient.createClient(googleApiBaseUrl);

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
        );
        ;

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
        );
        ;

//        String originCityResponse = restClient.post()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
//                        .queryParam("key", geminiApiKey)
//                        .build())
//                .body(originCityRequestBody)
//                .retrieve()
//                .body(String.class);
//
//        String destinationCityResponse = restClient.post()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
//                        .queryParam("key", geminiApiKey)
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

        var duffelResponse = flyService.getFlies(getFlightDto);

        var simplifiedFlight = flyService.getSimplifiedFlights(duffelResponse);

        var topFlights = simplifiedFlight.stream()
                .sorted(Comparator.comparing(FlightResponseDto::pricePerPerson))
                .limit(5)
                .toList();


        String tripPrompt = tripPromptService.generateTripPlan(topFlights);

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
        );
        ;

        String tripResponse = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemma-3-4b-it:generateContent")
                        .queryParam("key", geminiApiKey)
                        .build())
                .body(tripRequestBody)
                .retrieve()
                .body(String.class);

        return tripResponse;
    }
}