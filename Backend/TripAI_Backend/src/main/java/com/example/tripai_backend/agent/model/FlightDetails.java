package com.example.tripai_backend.agent.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record FlightDetails (
    String recommendedAirline,
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime outboundDeparture,
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime outboundArrival,
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime returnDeparture,
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime returnArrival
    ){}
