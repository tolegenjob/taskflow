package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.User;

import java.time.LocalDateTime;

public record RegisterResponse(
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt
) { public static RegisterResponse toDto(User user) {
        return new RegisterResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt());
    }
}
