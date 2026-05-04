package com.example.tripai_backend.agent.agent_interface;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface TripAgent {

    @SystemMessage("You are an expert travel planner.")
    String plan(@UserMessage String message);
}