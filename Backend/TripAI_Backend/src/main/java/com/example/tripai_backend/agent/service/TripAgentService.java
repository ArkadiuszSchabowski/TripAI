package com.example.tripai_backend.agent.service;

import com.example.tripai_backend.agent.agent_interface.TripAgent;
import com.example.tripai_backend.agent.model.AgentTripPlanDto;
import com.example.tripai_backend.agent.prompt.TripAgentPrompt;
import com.example.tripai_backend.agent.tool.FlightTool;
import com.example.tripai_backend.agent.tool.IataTool;
import com.example.tripai_backend.model.trip.TripRequest;
import com.example.tripai_backend.agent.security.TripAgentRateLimiter;
import com.example.tripai_backend.validator.TripRequestValidator;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;

    @Service
    public class TripAgentService {

        private  final ChatLanguageModel model;
        private final FlightTool flightTool;
        private final IataTool iataTool;
        private final TripAgentPrompt tripAgentPrompt;
        private final TripAgentRateLimiter tripAgentRateLimiter;
        private final TripRequestValidator tripRequestValidator;

        public TripAgentService(ChatLanguageModel model,
                                IataTool iataTool,
                                FlightTool flightTool,
                                TripAgentPrompt tripAgentPrompt,
                                TripAgentRateLimiter tripAgentRateLimiter,
                                TripRequestValidator tripRequestValidator)
        {
            this.flightTool = flightTool;
            this.iataTool = iataTool;
            this.model = model;
            this.tripAgentPrompt = tripAgentPrompt;
            this.tripAgentRateLimiter = tripAgentRateLimiter;
            this.tripRequestValidator = tripRequestValidator;
        }

        public AgentTripPlanDto planTrip(TripRequest request) {

            tripRequestValidator.validateDto(request);
            tripAgentRateLimiter.validateAgentInvocationLimit();

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
