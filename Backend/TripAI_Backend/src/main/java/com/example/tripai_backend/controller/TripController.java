package com.example.tripai_backend.controller;

import com.example.tripai_backend.model.trip.TripRequest;
import com.example.tripai_backend.service.TripService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public String showTripPlan(@RequestBody TripRequest request) {
        return tripService.generateTripPlan(request);
    }
}
