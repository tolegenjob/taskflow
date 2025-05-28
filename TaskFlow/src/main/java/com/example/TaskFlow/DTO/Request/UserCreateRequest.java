package com.example.TaskFlow.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest (

        @NotBlank(message = "First name is required and should not be whitespace")
        String firstName,

        @NotBlank(message = "Last name is required and should not be whitespace")
        String lastName,

        @Email(message = "Email is required and should be in correct format")
        String email
) {
}
