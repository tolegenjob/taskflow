package com.example.TaskFlow.DTO.Response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
