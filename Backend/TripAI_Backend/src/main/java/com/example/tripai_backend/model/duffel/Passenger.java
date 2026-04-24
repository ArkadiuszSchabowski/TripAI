package com.example.tripai_backend.model.duffel;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Passenger(
        @JsonProperty("type") String type
) {}