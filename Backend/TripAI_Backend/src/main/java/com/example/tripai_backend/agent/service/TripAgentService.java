package com.example.tripai_backend.agent.service;

import com.example.tripai_backend.agent.agent_interface.TripAgent;
import com.example.tripai_backend.agent.model.AgentTripPlanDto;
import com.example.tripai_backend.agent.prompt.TripAgentPrompt;
import com.example.tripai_backend.agent.tool.FlightTool;
import com.example.tripai_backend.agent.tool.IataTool;
import com.example.tripai_backend.helpers.TextNormalizer;
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
        private final TextNormalizer textNormalizer;
        private final TripAgentPrompt tripAgentPrompt;
        private final TripAgentRateLimiter tripAgentRateLimiter;
        private final TripRequestValidator tripRequestValidator;

        public TripAgentService(ChatLanguageModel model,
                                IataTool iataTool,
                                FlightTool flightTool,
                                TextNormalizer textNormalizer,
                                TripAgentPrompt tripAgentPrompt,
                                TripAgentRateLimiter tripAgentRateLimiter,
                                TripRequestValidator tripRequestValidator)
        {
            this.flightTool = flightTool;
            this.iataTool = iataTool;
            this.model = model;
            this.textNormalizer = textNormalizer;
            this.tripAgentPrompt = tripAgentPrompt;
            this.tripAgentRateLimiter = tripAgentRateLimiter;
            this.tripRequestValidator = tripRequestValidator;
        }

        public AgentTripPlanDto planTrip(TripRequest request) {

            String formattedOriginCity = textNormalizer.normalize(request.originCity());
            String formattedDestinationCity = textNormalizer.normalize(request.destinationCity());

            TripRequest formattedRequest = new TripRequest(
                    request.fromDepartureDate(),
                    request.toDepartureDate(),
                    formattedOriginCity,
                    formattedDestinationCity,
                    request.numberOfPeople()
            );

            tripAgentRateLimiter.validateAgentInvocationLimit();
            tripRequestValidator.validateDto(formattedRequest);

            String userMessage = tripAgentPrompt.generateTripPlanPromptForAgent(formattedRequest);

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
