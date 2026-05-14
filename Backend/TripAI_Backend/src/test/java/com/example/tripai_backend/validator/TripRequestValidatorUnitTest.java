package com.example.tripai_backend.validator;

import com.example.tripai_backend.model.trip.TripRequest;
import com.example.tripai_backend.service.CityValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.tripai_backend.exception.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class TripRequestValidatorUnitTest {

    private static final String CORRECT_ORIGIN_CITY = "Warsaw";
    private static final String CORRECT_DESTINATION_CITY = "Berlin";
    private static final int CORRECT_NUMBER_OF_PEOPLE = 1;
    private static final String TOO_LONG_ORIGIN_CITY = "A".repeat(51);
    private static final String TOO_LONG_DESTINATION_CITY = "B".repeat(51);
    private static final LocalDate DATE_IN_THE_PAST =
            LocalDate.of(2000, 1, 1);
    private static final LocalDate END_TRIP_DATE_BEFORE_START =
            LocalDate.now().plusDays(1);

    private  static final LocalDate CORRECT_FROM_DEPARTURE_DATE = LocalDate.now();
    private  static final LocalDate CORRECT_TO_DEPARTURE_DATE = LocalDate.now().plusDays(1);
    private  static final LocalDate TOO_LONG_DURATION_AFTER_START_DATE = LocalDate.now().plusDays(15);

    private static final LocalDate START_TRIP_DATE_AFTER_END =
            LocalDate.now().plusDays(2);

    private static final int TOO_LOW_PEOPLE = 0;
    private static final int TOO_MANY_PEOPLE = 11;

    @Mock
    private CityValidationService cityValidationService;

    @InjectMocks
    private TripRequestValidator tripRequestValidator;

    @Test
    void validateDto_WhenNullDto_ThrowsBadRequestException() {
        TripRequest request = (TripRequest)null;

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Request body is required.", exception.getMessage());
    }

    @Test
    void validateDto_WhenFieldsAreMissing_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                null,
                CORRECT_TO_DEPARTURE_DATE,
                CORRECT_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                CORRECT_NUMBER_OF_PEOPLE
        );

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("All fields are required.", exception.getMessage());
    }

    @Test
    void validateDto_WhenTooLongOriginCity_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                CORRECT_FROM_DEPARTURE_DATE,
                CORRECT_TO_DEPARTURE_DATE,
                TOO_LONG_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                CORRECT_NUMBER_OF_PEOPLE
        );

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Origin city name is too long.", exception.getMessage());

    }

    @Test
    void validateDto_WhenTooLongDestinationCity_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                CORRECT_FROM_DEPARTURE_DATE,
                CORRECT_TO_DEPARTURE_DATE,
                CORRECT_ORIGIN_CITY,
                TOO_LONG_DESTINATION_CITY,
                CORRECT_NUMBER_OF_PEOPLE
        );

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Destination city name is too long.", exception.getMessage());
    }

    @Test
    void validateDto_WhenStartDateInThePast_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                DATE_IN_THE_PAST,
                CORRECT_TO_DEPARTURE_DATE,
                CORRECT_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                CORRECT_NUMBER_OF_PEOPLE
        );

        when(cityValidationService.exists(CORRECT_ORIGIN_CITY)).thenReturn(true);
        when(cityValidationService.exists(CORRECT_DESTINATION_CITY)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Start date cannot be in the past.", exception.getMessage());
    }

    @Test
    void validateDto_WhenEndDateInThePast_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                CORRECT_FROM_DEPARTURE_DATE,
                DATE_IN_THE_PAST,
                CORRECT_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                CORRECT_NUMBER_OF_PEOPLE
        );

        when(cityValidationService.exists(CORRECT_ORIGIN_CITY)).thenReturn(true);
        when(cityValidationService.exists(CORRECT_DESTINATION_CITY)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("End date cannot be in the past.", exception.getMessage());
    }

    @Test
    void validateDto_WhenEndTripDateBeforeStartTripDate_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                START_TRIP_DATE_AFTER_END,
                END_TRIP_DATE_BEFORE_START,
                CORRECT_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                CORRECT_NUMBER_OF_PEOPLE
        );

        when(cityValidationService.exists(CORRECT_ORIGIN_CITY)).thenReturn(true);
        when(cityValidationService.exists(CORRECT_DESTINATION_CITY)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Start trip date must be before end trip date.", exception.getMessage());
    }

    @Test
    void validateDto_WhenTooLongTripDuration_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                CORRECT_FROM_DEPARTURE_DATE,
                TOO_LONG_DURATION_AFTER_START_DATE,
                CORRECT_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                CORRECT_NUMBER_OF_PEOPLE
        );

        when(cityValidationService.exists(CORRECT_ORIGIN_CITY)).thenReturn(true);
        when(cityValidationService.exists(CORRECT_DESTINATION_CITY)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Trip duration must be 14 days or less.", exception.getMessage());
    }

    @Test
    void validateDto_WhenToLowNumberOfPeople_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                CORRECT_FROM_DEPARTURE_DATE,
                CORRECT_TO_DEPARTURE_DATE,
                CORRECT_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                TOO_LOW_PEOPLE
        );

        when(cityValidationService.exists(CORRECT_ORIGIN_CITY)).thenReturn(true);
        when(cityValidationService.exists(CORRECT_DESTINATION_CITY)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Number of people must be between 1 and 10.", exception.getMessage());
    }

    @Test
    void validateDto_WhenToManyNumberOfPeople_ThrowsBadRequestException() {
        TripRequest request = new TripRequest(
                CORRECT_FROM_DEPARTURE_DATE,
                CORRECT_TO_DEPARTURE_DATE,
                CORRECT_ORIGIN_CITY,
                CORRECT_DESTINATION_CITY,
                TOO_MANY_PEOPLE
        );

        when(cityValidationService.exists(CORRECT_ORIGIN_CITY)).thenReturn(true);
        when(cityValidationService.exists(CORRECT_DESTINATION_CITY)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> tripRequestValidator.validateDto(request)
        );

        assertEquals("Number of people must be between 1 and 10.", exception.getMessage());
    }
}
