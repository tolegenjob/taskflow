package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Request.RegisterRequest;
import com.example.TaskFlow.DTO.Request.UserUpdateRequest;
import com.example.TaskFlow.Entity.User;
import com.example.TaskFlow.Enum.Role;
import com.example.TaskFlow.Message.Producer.RedisMessageProducer;
import com.example.TaskFlow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;
import static com.example.TaskFlow.Util.MessageUtil.sendNotificationEventToRedis;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisMessageProducer redisMessageProducer;
    private final PasswordEncoder passwordEncoder;

    @CachePut(value = "users", key = "#result.id")
    public User createUser(RegisterRequest request, Role role) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        user.setActive(true);
        User saved = userRepository.save(user);
        log.info("Created user with id: {}", saved.getId());
        return saved;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        log.info("Got all users");
        return users;
    }

    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        User user = findOrThrow(userRepository, id, "User");
        log.info("Got user with id: {} ", id);
        return user;
    }

    @CachePut(value = "users", key = "#result.id")
    public User updateUserById(Long id, UserUpdateRequest userUpdateRequest) {
        User user = findOrThrow(userRepository, id, "User");
        user.setActive(userUpdateRequest.active());
        User saved = userRepository.save(user);
        log.info("Updated user with id: {} ", id);
        sendNotificationEventToRedis(
                redisMessageProducer,
                saved.getId(),
                saved.getEmail(),
                "USER UPDATED");
        return saved;
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUserById(Long id) {
        User user = findOrThrow(userRepository, id, "User");
        userRepository.deleteById(id);
        log.info("Deleted user with id: {} ", id);
        sendNotificationEventToRedis(
                redisMessageProducer,
                user.getId(),
                user.getEmail(),
                "USER DELETED");
    }

}
