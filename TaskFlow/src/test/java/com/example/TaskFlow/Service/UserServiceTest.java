package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Request.UserCreateRequest;
import com.example.TaskFlow.Entity.User;
import com.example.TaskFlow.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User expectedUser;
    private UserCreateRequest userCreateRequest;

    @BeforeEach
    void setUp() {
        expectedUser = new User();
        ReflectionTestUtils.setField(expectedUser, "id", 1L);
        expectedUser.setFirstName("John");
        expectedUser.setLastName("Doe");
        expectedUser.setEmail("john.doe@example.com");
        userCreateRequest = new UserCreateRequest(
                "John", "Doe", "john.doe@example.com"
        );
    }

    @Test
    void createUser_success() {
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        User result = userService.createUser(userCreateRequest);
        assertAll("Create User",
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getFirstName()).isEqualTo("John"),
                () -> assertThat(result.getLastName()).isEqualTo("Doe"),
                () -> assertThat(result.getEmail()).isEqualTo("john.doe@example.com")
        );
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        User result = userService.getUserById(1L);
        assertAll("Get User",
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getFirstName()).isEqualTo("John"),
                () -> assertThat(result.getLastName()).isEqualTo("Doe"),
                () -> assertThat(result.getEmail()).isEqualTo("john.doe@example.com")
        );
    }
}
