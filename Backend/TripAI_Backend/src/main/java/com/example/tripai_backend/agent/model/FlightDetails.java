package com.example.tripai_backend.agent.model;

public record FlightDetails (
    String recommendedAirline,
    String outboundDeparture,
    String outboundArrival,
    String returnDeparture,
    String returnArrival
    ){}
