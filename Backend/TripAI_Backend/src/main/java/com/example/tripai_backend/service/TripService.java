package com.example.tripai_backend.service;

import com.example.tripai_backend.FlyService;
import com.example.tripai_backend.config.AIConfig;
import com.example.tripai_backend.model.Flight.GetFlightDto;
import com.example.tripai_backend.model.Trip.TripRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TripService {

    private final FlyService flyService;
    private final AIConfig aiConfig;

    public TripService(AIConfig aiConfig, FlyService flyService)
    {
        this.flyService = flyService;
        this.aiConfig = aiConfig;
    }

    public String generateTripPlan(AIConfig aiConfig, TripRequest information) {

//        Create Model
        OpenAiChatModel model = aiConfig.CreateModel();

        var originCityPrompt = "Zamień nazwę miasta na jego skrót IATACityCode: " + information.originCity();
        var destinationCityPrompt = "Zamień nazwę miasta na jego skrót IATACityCode: " + information.destinationCity();

        String originCity = model.generate(originCityPrompt);
        String destinationCity = model.generate(destinationCityPrompt);

        var getFlightDto = new GetFlightDto(
                originCity,
                destinationCity,
                information.fromDepartureDate(),
                information.toDepartureDate()
        );

        //Pobierz loty
        var duffelResponse = flyService.getFlies(getFlightDto);

        //Filtracja wyników

        //Wymusic odpowiedź w formacie JSON

        String prompt3 = "Zaplanuj podróż z lotniska: na podstawie duffelResponse " + information.fromCity() + "do" +
                information.toCity() + "dla" + information.numberOfPeople() + " osób." + "Wylot: " +
                information.fromDate() + "Przylot: " + information.toDate() + "Oszaczuj koszty." + "Daj zwięzłe informacje.";

        return model.generate(prompt3);
    }
}
