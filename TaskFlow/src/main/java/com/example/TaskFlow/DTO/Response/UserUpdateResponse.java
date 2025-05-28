package com.example.TaskFlow.DTO.Response;

import com.example.TaskFlow.Entity.User;

import java.time.LocalDateTime;

public record UserUpdateResponse (

        Long id,
        String firstName,
        String lastName,
        String email,
        boolean active,
        LocalDateTime updatedAt

) { public static UserUpdateResponse toDTO(User user) {
        return new UserUpdateResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getActive(),
            user.getUpdatedAt());
    }
}
