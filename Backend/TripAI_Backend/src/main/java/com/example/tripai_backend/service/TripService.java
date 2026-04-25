package com.example.tripai_backend.service;

import com.example.tripai_backend.architecture.Gemini;
import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.example.tripai_backend.model.trip.TripRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TripService {

    private final FlyService flyService;
    private final CityPrompt cityPromptService;
    private final TripPrompt tripPromptService;
    private final Gemini gemini;

    public TripService(Gemini gemini,
                       CityPrompt cityPromptService,
                       TripPrompt tripPromptService,
                       FlyService flyService) {

        this.gemini = gemini;
        this.cityPromptService = cityPromptService;
        this.flyService = flyService;
        this.tripPromptService = tripPromptService;
    }

    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.destinationCity()).trim();

        Map<String, Object> originCityRequestBody = gemini.createBody(originCityPrompt);

        Map<String, Object> destinationCityRequestBody = gemini.createBody(destinationCityPrompt);

        String originCityResponse = gemini.callApi(originCityRequestBody);

        String destinationCityResponse = gemini.callApi(destinationCityRequestBody);

        String originCity = gemini.getTextFromJson(originCityResponse);

        String destinationCity = gemini.getTextFromJson(destinationCityResponse);

        var getFlightDto = new GetFlightDto(
                originCity,
                destinationCity,
                tripRequest.fromDepartureDate(),
                tripRequest.toDepartureDate()
        );

        List<FlightResponseDto> topFlights = flyService.GetTopFiveFlights(getFlightDto);

        String tripPrompt = tripPromptService.generateTripPlan(topFlights, tripRequest.numberOfPeople());

        Map<String, Object> tripRequestBody = gemini.createBody(tripPrompt);

        String tripResponse = gemini.callApi(tripRequestBody);

        return gemini.getTextFromJson(tripResponse);
    }
}