package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Request.LoginRequest;
import com.example.TaskFlow.DTO.Request.RefreshTokenRequest;
import com.example.TaskFlow.DTO.Request.RegisterRequest;
import com.example.TaskFlow.DTO.Response.LoginResponse;
import com.example.TaskFlow.DTO.Response.RegisterResponse;
import com.example.TaskFlow.DTO.Response.TokenResponse;
import com.example.TaskFlow.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = RegisterResponse.toDto(authService.register(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        TokenResponse response = new TokenResponse(authService.refreshToken(request), request.refreshToken());
        return ResponseEntity.ok(response);
    }

}
