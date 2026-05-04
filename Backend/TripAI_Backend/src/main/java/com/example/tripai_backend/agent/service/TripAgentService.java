package com.example.tripai_backend.agent.service;

import com.example.tripai_backend.agent.agent_interface.TripAgent;
import com.example.tripai_backend.agent.prompt.TripAgentPrompt;
import com.example.tripai_backend.agent.tool.FlightTool;
import com.example.tripai_backend.agent.tool.IataTool;
import com.example.tripai_backend.model.trip.TripRequest;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;

    @Service
    public class TripAgentService {

        private final TripAgentPrompt tripAgentPrompt;
        private final IataTool iataTool;
        private final FlightTool flightTool;
        private  final ChatLanguageModel model;

        public TripAgentService(ChatLanguageModel model, TripAgentPrompt tripAgentPrompt, IataTool iataTool,
                                FlightTool flightTool) {

            this.model = model;
            this.tripAgentPrompt = tripAgentPrompt;
            this.iataTool = iataTool;
            this.flightTool = flightTool;
        }

        public String planTrip(TripRequest request) {

            String userMessage = tripAgentPrompt.generateTripPlanPromptForAgent(request);

            TripAgent agent = AiServices.builder(TripAgent.class)
                    .chatLanguageModel(model)
                    .tools(
                            iataTool,
                            flightTool
                    )
                    .build();

            return agent.plan(userMessage);
        }

    }
