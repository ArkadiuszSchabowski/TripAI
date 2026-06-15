package com.example.tripai_backend.integration;

import com.example.tripai_backend.agent.tool.FlightTool;
import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.model.flight.FlightResponseDto;
import com.example.tripai_backend.model.flight.GetFlightDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FlightToolIntegrationTest {

    @MockitoBean
    private DuffelClient mockDuffelClient;

    @Autowired
    private FlightTool flightTool;

    @Test
    public void getFlights_ShouldReturns_TopFiveFlights() {

        String duffelResponse;
        try {
            duffelResponse = Files.readString(Path.of("src/test/resources/duffel_response.json"));
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się odczytać pliku JSON", e);
        }

        String originIataCode = "BCN";
        String destinationIataCode = "BER";
        String fromDepartureDate = "2026-07-01";
        String toDepartureDate = "2026-07-04";

        GetFlightDto expectedDto = new GetFlightDto(
                originIataCode,
                destinationIataCode,
                LocalDate.parse(fromDepartureDate),
                LocalDate.parse(toDepartureDate)
        );

        when(mockDuffelClient.getFlights(expectedDto)).thenReturn(duffelResponse);

        var result = flightTool.getFlights(originIataCode, destinationIataCode, fromDepartureDate, toDepartureDate);

        List<FlightResponseDto> expectedResult = List.of(
                new FlightResponseDto(
                        "Berlin Brandenburg Airport",
                        "Barcelona–El Prat Josep Tarradellas Airport",
                        "2026-07-01T10:35:00",
                        "2026-07-01T13:20:00",
                        "2026-07-04T05:50:00",
                        "2026-07-04T08:35:00",
                        "Vueling",
                        60.28
                ),
                new FlightResponseDto(
                        "Berlin Brandenburg Airport",
                        "Barcelona–El Prat Josep Tarradellas Airport",
                        "2026-07-01T13:50:00",
                        "2026-07-01T16:35:00",
                        "2026-07-04T05:50:00",
                        "2026-07-04T08:35:00",
                        "Vueling",
                        60.28
                ),
                new FlightResponseDto(
                        "Berlin Brandenburg Airport",
                        "Barcelona–El Prat Josep Tarradellas Airport",
                        "2026-07-01T22:00:00",
                        "2026-07-02T00:45:00",
                        "2026-07-04T05:50:00",
                        "2026-07-04T08:35:00",
                        "Vueling",
                        60.28
                ),
                new FlightResponseDto(
                        "Berlin Brandenburg Airport",
                        "Barcelona–El Prat Josep Tarradellas Airport",
                        "2026-07-01T10:35:00",
                        "2026-07-01T13:20:00",
                        "2026-07-04T10:20:00",
                        "2026-07-04T13:05:00",
                        "Vueling",
                        60.28
                ),
                new FlightResponseDto(
                        "Berlin Brandenburg Airport",
                        "Barcelona–El Prat Josep Tarradellas Airport",
                        "2026-07-01T13:50:00",
                        "2026-07-01T16:35:00",
                        "2026-07-04T10:20:00",
                        "2026-07-04T13:05:00",
                        "Vueling",
                        60.28
                )
        );
        assertEquals(expectedResult, result);
    }
}
