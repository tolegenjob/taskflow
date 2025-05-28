package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.User;

import java.time.LocalDateTime;

public record UserResponse (

        Long id,
        String firstName,
        String lastName,
        String email,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) { public static UserResponse toDTO(User user) {
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getActive(),
            user.getCreatedAt(),
            user.getUpdatedAt());
    }
}
