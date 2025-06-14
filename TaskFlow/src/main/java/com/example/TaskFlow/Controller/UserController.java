package com.example.TaskFlow.Controller;

import com.example.TaskFlow.DTO.Request.RegisterRequest;
import com.example.TaskFlow.DTO.Request.UserUpdateRequest;
import com.example.TaskFlow.DTO.Response.UserCreateResponse;
import com.example.TaskFlow.DTO.Response.UserResponse;
import com.example.TaskFlow.DTO.Response.UserUpdateResponse;
import com.example.TaskFlow.Enum.Role;
import com.example.TaskFlow.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/{role}")
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody @Valid RegisterRequest request,
            @PathVariable Role role
    ) {
        UserCreateResponse userCreateResponse = UserCreateResponse.toDTO(
                userService.createUser(request, role));
        return ResponseEntity.ok(userCreateResponse);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserResponse> userResponses = userService.getAllUsers(pageable)
                .map(UserResponse::toDTO);
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = UserResponse.toDTO(userService.getUserById(id));
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUserById(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest
    ) {
        UserUpdateResponse userUpdateResponse = UserUpdateResponse.toDTO(userService.updateUserById(id, userUpdateRequest));
        return ResponseEntity.ok(userUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
