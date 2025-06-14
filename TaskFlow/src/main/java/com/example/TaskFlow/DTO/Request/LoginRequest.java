package com.example.TaskFlow.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Email(message = "Email is required and should be in correct format")
        String email,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 8, max = 100, message = "Password must be at least 8 symbols")
        String password

) {
}
