package com.example.TaskFlow.Exception;


public class FileNotValidException extends RuntimeException {
    public FileNotValidException(String errorMessage) {
        super(errorMessage);
    }
}
