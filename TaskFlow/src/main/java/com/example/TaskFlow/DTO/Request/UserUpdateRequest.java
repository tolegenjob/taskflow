package com.example.TaskFlow.DTO.Request;

import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest (

        @NotNull(message = "User should be either active or not")
        boolean active
) {
}
