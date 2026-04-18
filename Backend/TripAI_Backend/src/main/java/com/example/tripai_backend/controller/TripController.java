package com.example.tripai_backend.controller;

import com.example.tripai_backend.model.TripRequest;
import com.example.tripai_backend.service.TripService;
import dev.langchain4j.model.openai.OpenAiChatModel;
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

    @GetMapping("/test")
    public String test() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai/")
                .apiKey("SECRET")
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        return model.generate("Cześć Gemini! Jeśli to widzisz, to znaczy, że most OpenAI zadziałał.");
    }
}