package com.example.tripai_backend.service;

import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.mapper.DuffelFlightMapper;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceUnitTest {

    @Mock
    private DuffelClient mockDuffelClient;

    @Mock
    private DuffelFlightMapper mockDuffelFlightMapper;

    @InjectMocks
    private FlightService flightService;

    @Test
    void getFlights_withCorrectValues_shouldInvokeClientGetFlightsOnce() {

        String duffelResponse = "duffel response string";

        String originIataCode = "WAW";
        String destinationIataCode = "BER";
        LocalDate fromDepartureDate = LocalDate.of(2030, 1, 1);
        LocalDate toDepartureDate =  LocalDate.of(2030, 1, 2);

        GetFlightDto dto = new GetFlightDto(
                originIataCode,
                destinationIataCode,
                fromDepartureDate,
                toDepartureDate
        );

        when(mockDuffelClient.getFlights(dto)).thenReturn(duffelResponse);

        flightService.getFlights(dto);

        verify(mockDuffelClient, times(1)).getFlights(dto);
    }

    @Test
    void getSimplifiedFlights_withCorrectValues_shouldInvokeMapToFlightsOnce() {

        String json = "correct duffel json";

        List<FlightResponseDto> flights = new ArrayList<>();

        when(mockDuffelFlightMapper.mapToFlights(json)).thenReturn(flights);

        flightService.getSimplifiedFlights(json);

        verify(mockDuffelFlightMapper, times(1)).mapToFlights(json);
    }

    @Test
    void getTopFlights_WithEmptyList_ShouldReturnEmptyList(){

        List<FlightResponseDto> flights = new ArrayList<>();

        List<FlightResponseDto> result = flightService.getTopFlights(flights);

        assertEquals(0, result.size());
    }

    @Test
    void getTopFlights_WhenListLongerThanFive_ShouldReturnFiveCheapestFlights(){

        List<FlightResponseDto> flights = List.of(
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "LOT", 210.00),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Lufthansa", 320.00),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Ryanair", 89.99),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Emirates", 2500.00),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Air France", 450.50),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Wizz Air", 150.00)
        );

        List<FlightResponseDto> expectedResult = List.of(
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Ryanair", 89.99),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Wizz Air", 150.00),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "LOT", 210.00),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Lufthansa", 320.00),
                new FlightResponseDto("Warszawa", "Berlin", "2026-05-01", "2026-05-10", "Air France", 450.50)
        );

        List<FlightResponseDto> result = flightService.getTopFlights(flights);

        assertEquals(expectedResult, result);
    }
}