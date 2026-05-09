package com.example.tripai_backend.service;

import com.example.tripai_backend.architecture.FlightFacade;
import com.example.tripai_backend.architecture.GeminiFacade;
import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.example.tripai_backend.model.trip.TripRequest;
import com.example.tripai_backend.security.TripRateLimiter;
import com.example.tripai_backend.validator.TripRequestValidator;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TripService {

    private final CityPrompt cityPromptService;
    private final FlightFacade flight;
    private final GeminiFacade gemini;
    private final TripPrompt tripPromptService;
    private final TripRateLimiter tripRateLimiter;
    private final TripRequestValidator tripRequestValidator;

    public TripService(CityPrompt cityPromptService,
                       FlightFacade flight,
                       GeminiFacade gemini,
                       TripPrompt tripPromptService,
                       TripRateLimiter tripRateLimiter,
                       TripRequestValidator tripRequestValidator)
    {
        this.cityPromptService = cityPromptService;
        this.flight = flight;
        this.gemini = gemini;
        this.tripPromptService = tripPromptService;
        this.tripRateLimiter = tripRateLimiter;
        this.tripRequestValidator = tripRequestValidator;
    }
    public String generateTripPlan(TripRequest tripRequest) {

        tripRequestValidator.validateDto(tripRequest);
        tripRateLimiter.validateInvocationLimit();

        String originCityPrompt = cityPromptService.generateIataPromptForCity(tripRequest.originCity());
        String destinationCityPrompt = cityPromptService.generateIataPromptForCity(tripRequest.destinationCity());

        String originIata = gemini.getIataCode(originCityPrompt);
        String destinationIata = gemini.getIataCode(destinationCityPrompt);

        GetFlightDto getFlightDto = new GetFlightDto(
                originIata,
                destinationIata,
                tripRequest.fromDepartureDate(),
                tripRequest.toDepartureDate()
        );

        List<FlightResponseDto> topFlights = flight.getTopFiveFlights(getFlightDto);

        String tripPrompt = tripPromptService.generateTripPlanPrompt(topFlights, tripRequest);

        return gemini.generateTripPlan(tripPrompt);
    }
}