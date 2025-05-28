package com.example.eventconsumerservice.Exception;

public class BadEventException extends RuntimeException {
    public BadEventException(String message) {
        super(message);
    }
}
