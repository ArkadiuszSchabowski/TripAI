package com.example.tripai_backend.model.duffel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DuffelOfferRequest(
        @JsonProperty("slices") List<Slice> slices,
        @JsonProperty("passengers") List<Passenger> passengers,
        @JsonProperty("cabin_class") String cabinClass
) {}