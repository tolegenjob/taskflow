package com.example.TaskFlow.Exception;

public class DoesNotBelongToEntityException extends RuntimeException {
    public DoesNotBelongToEntityException(String errorMessage) {
        super(errorMessage);
    }
}
