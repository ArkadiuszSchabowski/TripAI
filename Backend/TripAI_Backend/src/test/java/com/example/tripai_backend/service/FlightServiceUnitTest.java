package com.example.tripai_backend.service;

import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.mapper.DuffelFlightMapper;
import com.example.tripai_backend.model.flight.GetFlightDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
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
    void getFlights_withCorrectValues_shouldClientGetFlightsOnce() {

        String duffelResponse = "duffel response string";

        String originIataCode = "WAW";
        String destinationIataCode = "BER";
        LocalDate fromDepartureDate = LocalDate.of(2030, 1, 1);
        LocalDate toDepartureDFate =  LocalDate.of(2030, 1, 2);

        GetFlightDto dto = new GetFlightDto(
                originIataCode,
                destinationIataCode,
                fromDepartureDate,
                toDepartureDFate
        );

        when(mockDuffelClient.getFlights(any())).thenReturn(duffelResponse);

        flightService.getFlights(dto);

        verify(mockDuffelClient, times(1)).getFlights(dto);
    }
}