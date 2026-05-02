package com.example.tripai_backend.middleware;

import com.example.tripai_backend.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    @RestControllerAdvice
public class ErrorHandlingMiddleware {

        private static final Logger log =
                LoggerFactory.getLogger(ErrorHandlingMiddleware.class);

        @ExceptionHandler(BadRequestException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String handleBadRequest(BadRequestException ex) {
            log.error("BadRequestException: ", ex);
            return ex.getMessage();
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String handleInvalidJson(HttpMessageNotReadableException ex) {
            log.error("Invalid JSON format or request body: ", ex);
            return "Invalid JSON format or request body.";
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public String handleGeneral(Exception ex) {
            log.error("Unhandled server exception: ", ex);
            return "Unexpected server error. Try again later.";
        }
    }