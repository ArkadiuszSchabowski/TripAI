package com.example.tripai_backend.validator;

import com.example.tripai_backend.exception.BadRequestException;
import com.example.tripai_backend.model.trip.TripRequest;
import com.example.tripai_backend.service.CityValidationService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TripRequestValidator {

    private static final int MAX_TRIP_DAYS = 14;
    private static final int MIN_PEOPLE = 1;
    private static final int MAX_PEOPLE = 10;
    private static final int MAX_ORIGIN_CITY_LENGTH = 50;
    private static final int MAX_DESTINATION_CITY_LENGTH = 50;
    private final CityValidationService cityValidationService;

    public TripRequestValidator(CityValidationService cityValidationService) {
        this.cityValidationService = cityValidationService;
    }

    public void validateDto(TripRequest request) {

        LocalDate today = LocalDate.now();

        if (request == null) {
            throw new BadRequestException("Request body is required.");
        }

        if (request.originCity() == null || request.originCity().isBlank() ||
                request.destinationCity() == null || request.destinationCity().isBlank() ||
                request.fromDepartureDate() == null ||
                request.toDepartureDate() == null) {

            throw new BadRequestException("All fields are required.");
        }

        if (request.originCity().length() > MAX_ORIGIN_CITY_LENGTH) {
            throw new BadRequestException("Origin city name is too long.");
        }

        if (request.destinationCity().length() > MAX_DESTINATION_CITY_LENGTH) {
            throw new BadRequestException("Destination city name is too long.");
        }

        if (!cityValidationService.exists(request.originCity())) {
            throw new BadRequestException("We couldn't find the departure city.");
        }

        if (!cityValidationService.exists(request.destinationCity())) {
            throw new BadRequestException("We couldn't find the destination city.");
        }

        if (request.fromDepartureDate().isBefore(today)) {
            throw new BadRequestException("Start date cannot be in the past.");
        }

        if (request.toDepartureDate().isBefore(today)) {
            throw new BadRequestException("End date cannot be in the past.");
        }

        if (!request.fromDepartureDate()
                .isBefore(request.toDepartureDate())) {

            throw new BadRequestException("Start trip date must be before end trip date.");
        }

        if (request.fromDepartureDate()
                .plusDays(MAX_TRIP_DAYS)
                .isBefore(request.toDepartureDate())) {

            throw new BadRequestException("Trip duration must be 14 days or less.");
        }

        if (request.numberOfPeople() < MIN_PEOPLE ||
                request.numberOfPeople() > MAX_PEOPLE) {

            throw new BadRequestException("Number of people must be between 1 and 10.");
        }
    }
}
