package com.example.TaskFlow.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

        @NotBlank(message = "First name is required and should not be whitespace")
        String firstName,

        @NotBlank(message = "Last name is required and should not be whitespace")
        String lastName,

        @Email(message = "Email is required and should be in correct format")
        String email,

        @NotBlank(message = "Password must not be blank")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,100}$",
                message = "Password must be at least 8 symbols, including uppercase and lowercase letters, numbers and special characters [@$!%*?&]"
        )
        String password

) {
}
