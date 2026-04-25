package com.example.tripai_backend.service;

import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.client.GeminiClient;
import com.example.tripai_backend.helpers.BodyCreator;
import com.example.tripai_backend.mapper.GeminiMapper;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.example.tripai_backend.model.trip.TripRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TripService {

    private final GeminiClient geminiClient;
    private final FlyService flyService;
    private final CityPrompt cityPromptService;
    private final TripPrompt tripPromptService;
    private final BodyCreator bodyCreator;
    private final GeminiMapper geminiMapper;

    public TripService(BodyCreator bodyCreator, CityPrompt cityPromptService, GeminiClient geminiClient,
                       GeminiMapper geminiMapper, TripPrompt tripPromptService, FlyService flyService) {
        this.bodyCreator = bodyCreator;
        this.cityPromptService = cityPromptService;
        this.geminiClient = geminiClient;
        this.geminiMapper = geminiMapper;
        this.flyService = flyService;
        this.tripPromptService = tripPromptService;
    }

    @SneakyThrows
    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.destinationCity()).trim();

        Map<String, Object> originCityRequestBody = bodyCreator.createBody(originCityPrompt);

        Map<String, Object> destinationCityRequestBody = bodyCreator.createBody(destinationCityPrompt);

        String originCityResponse = geminiClient.callGeminiApi(originCityRequestBody);

        String destinationCityResponse = geminiClient.callGeminiApi(destinationCityRequestBody);

        String originCity = geminiMapper.getTextFromGeminiJson(originCityResponse);

        String destinationCity = geminiMapper.getTextFromGeminiJson(destinationCityResponse);

        var getFlightDto = new GetFlightDto(
                originCity,
                destinationCity,
                tripRequest.fromDepartureDate(),
                tripRequest.toDepartureDate()
        );

        String duffelResponse = flyService.getFlies(getFlightDto);

        List<FlightResponseDto> simplifiedFlight = flyService.getSimplifiedFlights(duffelResponse);

        List<FlightResponseDto> topFlights = flyService.getTopFlights(simplifiedFlight);

        String tripPrompt = tripPromptService.generateTripPlan(topFlights, tripRequest.numberOfPeople());

        Map<String, Object> tripRequestBody = bodyCreator.createBody(tripPrompt);

        String tripResponse = geminiClient.callGeminiApi(tripRequestBody);

        return geminiMapper.getTextFromGeminiJson(tripResponse);
    }
}