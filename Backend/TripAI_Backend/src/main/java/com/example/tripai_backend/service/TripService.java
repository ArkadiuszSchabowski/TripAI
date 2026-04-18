package com.example.tripai_backend.service;

import com.example.tripai_backend.model.TripRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    public String generateTripPlan(TripRequest information) {

        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai/")
                .apiKey("SECRET")
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        String prompt = "Zaplanuj podróż z lotniska: " + information.fromCity() + "do" +
                information.toCity() + "dla" + information.numberOfPeople() + " osób." + "Wylot: " +
                information.fromDate() + "Przylot: " + information.toDate() + "Oszaczuj koszty." + "Daj zwięzłe informacje.";

        return model.generate(prompt);
    }
}
