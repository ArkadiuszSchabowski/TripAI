package com.example.tripai_backend.model;

public record ErrorResponse(
        String message,
        int status
) {}