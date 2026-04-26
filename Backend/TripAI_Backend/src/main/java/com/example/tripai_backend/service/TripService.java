package com.example.tripai_backend.service;

import com.example.tripai_backend.architecture.FlightFacade;
import com.example.tripai_backend.architecture.GeminiFacade;
import com.example.tripai_backend.aiPrompt.CityPrompt;
import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.example.tripai_backend.model.trip.TripRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TripService {

    private final CityPrompt cityPromptService;
    private final FlightFacade flight;
    private final GeminiFacade gemini;
    private final TripPrompt tripPromptService;

    public TripService(CityPrompt cityPromptService,
                       FlightFacade flight,
                       GeminiFacade gemini,
                       TripPrompt tripPromptService)
            {

        this.cityPromptService = cityPromptService;
        this.flight = flight;
        this.gemini = gemini;
        this.tripPromptService = tripPromptService;
    }

    public String generateTripPlan(TripRequest tripRequest) {

        String originCityPrompt = cityPromptService.generateIATAPromptForCity(tripRequest.originCity()).trim();
        String destinationCityPrompt = cityPromptService.generateIATAPromptForCity(tripRequest.destinationCity()).trim();

        String originCity = gemini.generateCityInfo(originCityPrompt);
        String destinationCity = gemini.generateCityInfo(destinationCityPrompt);

        var getFlightDto = new GetFlightDto(
                originCity,
                destinationCity,
                tripRequest.fromDepartureDate(),
                tripRequest.toDepartureDate()
        );

        List<FlightResponseDto> topFlights = flight.GetTopFiveFlights(getFlightDto);

        String tripPrompt = tripPromptService.generateTripPlanPrompt(topFlights, tripRequest.numberOfPeople());

        return gemini.generateTripPlan(tripPrompt);
    }
}