package com.example.tripai_backend.architecture;

import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import com.example.tripai_backend.service.FlightService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlightFacade {
    private final FlightService service;

    public FlightFacade(FlightService service) {
        this.service = service;
    }

    public List<FlightResponseDto> GetTopFiveFlights(GetFlightDto dto) {

        String response = service.getFlights(dto);
        List<FlightResponseDto> flights = service.getSimplifiedFlights(response);
        return service.getTopFlights(flights);
    }
}
