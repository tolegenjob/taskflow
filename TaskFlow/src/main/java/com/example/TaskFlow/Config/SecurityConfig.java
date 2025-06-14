package com.example.TaskFlow.Config;

import com.example.TaskFlow.Security.JwtFilter;
import com.example.TaskFlow.Security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()

                        .requestMatchers(
                                "/analytics/**",
                                "/migration-elastic/**",
                                "/events/**",
                                "/logs/**",
                                "/search/**",
                                "/users/**",
                                "/**" // fallback: protect all other endpoints
                        ).hasRole("ADMIN")


                        .requestMatchers(HttpMethod.POST, "/projects").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/projects").hasAnyRole("ADMIN", "SUPERVISOR")
                        .requestMatchers(HttpMethod.POST, "/projects/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/projects/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/projects/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/tasks").authenticated()
                        .requestMatchers(HttpMethod.GET, "/tasks").hasAnyRole("ADMIN", "SUPERVISOR")
                        .requestMatchers(HttpMethod.GET, "/tasks/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/tasks/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/tasks/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/tasks/filter").hasRole("ADMIN")

                        .requestMatchers("/tasks/*/comments/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/tasks/{taskId}/documents").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tasks/{taskId}/history").hasRole("ADMIN")

                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
