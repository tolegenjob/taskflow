package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.User;

import java.time.LocalDateTime;

public record UserCreateResponse (

        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt

) { public static UserCreateResponse toDTO(User user) {
        return new UserCreateResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getCreatedAt());
    }
}
