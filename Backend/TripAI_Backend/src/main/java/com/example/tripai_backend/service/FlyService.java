package com.example.tripai_backend.service;

import com.example.tripai_backend.client.DuffelClient;
import com.example.tripai_backend.model.Flight.GetFlightDto;
import org.springframework.stereotype.Service;

@Service
public class FlyService {

    private final DuffelClient duffelClient;

    public FlyService(DuffelClient duffelClient) {
        this.duffelClient = duffelClient;
    }

    public String getFlies(GetFlightDto getFlightDto) {
        return duffelClient.getFlights(getFlightDto);
    }
}
