package com.example.tripai_backend.middleware;

import com.example.tripai_backend.exception.AgentInvocationLimitReachedException;
import com.example.tripai_backend.exception.BadRequestException;
import com.example.tripai_backend.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    @RestControllerAdvice
public class ErrorHandlingMiddleware {

        private static final Logger log =
                LoggerFactory.getLogger(ErrorHandlingMiddleware.class);

        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse handleInvalidJson(HttpMessageNotReadableException ex) {
            log.error("HttpMessageNotReadableException", ex);
            return new ErrorResponse(
                    "Invalid JSON format or request body.",
                    400
            );
        }

        @ExceptionHandler(BadRequestException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse handleBadRequest(BadRequestException ex) {
            log.error("BadRequestException: ", ex);
            return new ErrorResponse(
                    ex.getMessage(),
                    400
            );
        }

        @ExceptionHandler(AgentInvocationLimitReachedException.class)
        @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
        public ErrorResponse handleAgentInvocationLimitReached(AgentInvocationLimitReachedException ex) {
            log.error("AgentInvocationLimitReachedException: ", ex);
            return new ErrorResponse(
                    ex.getMessage(),
                    429
            );
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorResponse handleGeneral(Exception ex) {
            log.error("Unhandled server exception: ", ex);
            return new ErrorResponse(
                    "Unexpected server error. Try again later.",
                    500
            );
        }
    }