package com.example.tripai_backend.controller;

import com.example.tripai_backend.model.trip.TripRequest;
import com.example.tripai_backend.agent.service.TripAgentService;
import com.example.tripai_backend.service.TripService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripAgentService tripAgentService;
    private final TripService tripService;

    public TripController(TripAgentService tripAgentService, TripService tripService) {
        this.tripAgentService = tripAgentService;
        this.tripService = tripService;
    }

    @PostMapping()
    public String showTripPlan(@RequestBody TripRequest request) {
        return tripService.generateTripPlan(request);
    }

    @PostMapping("with-agent")
    public String showAgentTripPlan(@RequestBody TripRequest request) {
        return tripAgentService.planTrip(request);
    }
}
