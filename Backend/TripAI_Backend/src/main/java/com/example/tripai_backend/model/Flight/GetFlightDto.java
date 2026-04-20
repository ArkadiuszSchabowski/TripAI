package com.example.tripai_backend.model.Flight;

import java.time.LocalDate;

public record GetFlightDto(
    String IATACityCodeOriginCity,
    String IATACityCodeDestinationCity,
    LocalDate fromDepartureDate,
    LocalDate toDepartureDate
){}
