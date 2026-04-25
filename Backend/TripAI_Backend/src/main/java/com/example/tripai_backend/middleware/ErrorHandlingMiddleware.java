package com.example.tripai_backend.middleware;

import com.example.tripai_backend.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

    @RestControllerAdvice
public class ErrorHandlingMiddleware {

        @ExceptionHandler(BadRequestException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String handleBadRequest(BadRequestException ex) {
            return ex.getMessage();
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String handleInvalidJson(HttpMessageNotReadableException ex) {
            return "Invalid JSON format or request body.";
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public String handleGeneral(Exception ex) {
            return "Unexpected server error. Try again later.";
        }
    }