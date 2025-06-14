package com.example.TaskFlow.Security;

import com.example.TaskFlow.Config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(UserDetails user) {
        log.info("Generating access token for user {}", user.getUsername());
        return Jwts.builder()
                .claim("roles", user.getAuthorities())
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(jwtProperties.accessExpirationMs(), ChronoUnit.MILLIS)))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        log.info("Generating refresh token for user {}", user.getUsername());
        return Jwts.builder()
                .claim("roles", user.getAuthorities())
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(jwtProperties.refreshExpirationMs(), ChronoUnit.MILLIS)))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        log.info("Checking if access token {} is valid", token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateRefreshToken(String token) {
        log.info("Checking if refresh token {} is valid", token);
        return !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        log.info("Extracting username from token {}", token);
        return extractAllClaimsFromToken(token).getSubject();
    }

    private SecretKey getSigningKey() {
        var key = jwtProperties.secret().getBytes();
        return Keys.hmacShaKeyFor(key);
    }

    private Claims extractAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractAllClaimsFromToken(token).getExpiration().before(new Date());
    }

}