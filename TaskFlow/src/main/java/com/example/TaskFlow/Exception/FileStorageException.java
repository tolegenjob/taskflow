package com.example.TaskFlow.Exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
