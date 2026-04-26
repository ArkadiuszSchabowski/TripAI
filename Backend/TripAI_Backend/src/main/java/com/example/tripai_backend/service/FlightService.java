package com.example.tripai_backend.service;

import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.mapper.DuffelFlightMapper;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class FlightService {

    private final DuffelClient duffelClient;
    private final DuffelFlightMapper duffelFlightMapper;

    public FlightService(DuffelClient duffelClient, DuffelFlightMapper duffelFlightMapper) {
        this.duffelClient = duffelClient;
        this.duffelFlightMapper = duffelFlightMapper;
    }

    public String getFlights(GetFlightDto getFlightDto) {
        return duffelClient.getFlights(getFlightDto);
    }

    public List<FlightResponseDto> getSimplifiedFlights(String duffelJson) {

        return duffelFlightMapper.mapToFlights(duffelJson);
    }

    public List<FlightResponseDto> getTopFlights(List<FlightResponseDto> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(FlightResponseDto::pricePerPerson))
                .limit(5)
                .toList();
    }
}
