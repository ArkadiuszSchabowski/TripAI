package com.example.tripai_backend.service;

import com.example.tripai_backend.aiPrompt.TripPrompt;
import com.example.tripai_backend.model.trip.TripRequest;
import org.springframework.stereotype.Service;

    @Service
    public class TripAgentService {

        private final TripPrompt tripPrompt;

        public TripAgentService(TripPrompt tripPrompt) {
            this.tripPrompt = tripPrompt;
        }

        public String planTrip(TripRequest request) {

            String userMessage = tripPrompt.generateTripPlanPromptForAgent(request);

            return "";
        }

    }
