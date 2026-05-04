package com.example.tripai_backend.agent.tool;

import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.example.tripai_backend.service.FlightService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Component
public class FlightTool {

    private final DuffelClient duffelClient;
    private final FlightService flightService;

    public FlightTool(DuffelClient duffelClient, FlightService flightService) {

        this.duffelClient = duffelClient;
        this.flightService = flightService;
    }

    @Tool("Get top five flights between two airports")
    public List<FlightResponseDto> getFlights(
            String originIataCode,
            String destinationIataCode,
            String fromDepartureDate,
            String toDepartureDate
    ) {

        GetFlightDto dto = new GetFlightDto(
                originIataCode,
                destinationIataCode,
                LocalDate.parse(fromDepartureDate),
                LocalDate.parse(toDepartureDate)
        );

        String duffelJson = duffelClient.getFlights(dto);

        List<FlightResponseDto> flights = flightService.getSimplifiedFlights(duffelJson);

        return flightService.getTopFlights(flights);
    }
}