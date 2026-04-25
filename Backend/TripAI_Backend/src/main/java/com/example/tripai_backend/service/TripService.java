package com.example.tripai_backend.service;

import com.example.tripai_backend.architecture.GeminiFacade;
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
    private final GeminiFacade geminiFacade;

    public TripService(GeminiFacade geminiFacade,
                       CityPrompt cityPromptService,
                       TripPrompt tripPromptService,
                       FlyService flyService) {

        this.geminiFacade = geminiFacade;
        this.cityPromptService = cityPromptService;
        this.flyService = flyService;
        this.tripPromptService = tripPromptService;
    }

    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPromptService.changeFromCityToIATACityCode(tripRequest.destinationCity()).trim();

        Map<String, Object> originCityRequestBody = geminiFacade.createBody(originCityPrompt);

        Map<String, Object> destinationCityRequestBody = geminiFacade.createBody(destinationCityPrompt);

        String originCityResponse = geminiFacade.callApi(originCityRequestBody);

        String destinationCityResponse = geminiFacade.callApi(destinationCityRequestBody);

        String originCity = geminiFacade.getTextFromJson(originCityResponse);

        String destinationCity = geminiFacade.getTextFromJson(destinationCityResponse);

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

        Map<String, Object> tripRequestBody = geminiFacade.createBody(tripPrompt);

        String tripResponse = geminiFacade.callApi(tripRequestBody);

        return geminiFacade.getTextFromJson(tripResponse);
    }
}