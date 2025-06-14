package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Request.LoginRequest;
import com.example.TaskFlow.DTO.Request.RefreshTokenRequest;
import com.example.TaskFlow.DTO.Request.RegisterRequest;
import com.example.TaskFlow.DTO.Response.LoginResponse;
import com.example.TaskFlow.Entity.User;
import com.example.TaskFlow.Enum.Role;
import com.example.TaskFlow.Exception.AlreadyExistsException;
import com.example.TaskFlow.Exception.TokenExpiredException;
import com.example.TaskFlow.Repository.UserRepository;
import com.example.TaskFlow.Security.JwtService;
import com.example.TaskFlow.Security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException("User with this email already exists");
        }
        User user = userService.createUser(request, Role.USER);
        log.info("User {} has been registered", user.getEmail());
        return user;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("User {} logged in", authentication.getName());
        return new LoginResponse(
                userDetails.getUsername(),
                jwtService.generateAccessToken(userDetails),
                jwtService.generateRefreshToken(userDetails)
        );
    }

    public String refreshToken(RefreshTokenRequest request) {
        String email  = jwtService.extractUsername(request.refreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        log.info("Refreshing token for user {}", email);
        if (!jwtService.validateRefreshToken(request.refreshToken())) {
            throw new TokenExpiredException("Refresh token expired");
        }
        return jwtService.generateAccessToken(userDetails);
    }

}
