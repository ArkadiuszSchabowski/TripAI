package com.example.tripai_backend.agent.agent_interface;
import com.example.tripai_backend.agent.model.AgentTripPlanDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface TripAgent {

    @SystemMessage("""
        You are a travel assistant. Your response must be a valid JSON object matching the requested structure.
        Do not include any conversational text, explanations, or markdown code blocks like json.
        """)
    AgentTripPlanDto plan(@UserMessage String message);
}