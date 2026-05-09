package com.example.tripai_backend.exception;

    public class InvocationLimitReachedException extends RuntimeException {
        public InvocationLimitReachedException(String message) {
            super(message);
        }
    }
