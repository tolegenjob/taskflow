package com.example.TaskFlow.DTO.Response;

public record LoginResponse(
        String email,
        String accessToken,
        String refreshToken
) {
}
