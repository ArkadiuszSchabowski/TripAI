package com.example.tripai_backend.exception;

    public class AgentInvocationLimitReachedException extends RuntimeException {
        public AgentInvocationLimitReachedException(String message) {
            super(message);
        }
    }
