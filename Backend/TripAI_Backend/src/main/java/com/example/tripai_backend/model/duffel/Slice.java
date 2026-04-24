package com.example.tripai_backend.model.duffel;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Slice(
        @JsonProperty("origin") String origin,
        @JsonProperty("destination") String destination,
        @JsonProperty("departure_date") String departureDate
) {}