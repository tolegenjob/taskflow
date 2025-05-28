package com.example.TaskFlow.Service;

import com.example.TaskFlow.DTO.Request.UserCreateRequest;
import com.example.TaskFlow.DTO.Request.UserUpdateRequest;
import com.example.TaskFlow.Entity.User;
import com.example.TaskFlow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserCreateRequest userCreateRequest) {
        User user = new User();
        user.setFirstName(userCreateRequest.firstName());
        user.setLastName(userCreateRequest.lastName());
        user.setEmail(userCreateRequest.email());
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

    public User getUserById(Long id) {
        User user = findOrThrow(userRepository, id, "User");
        log.info("Got user with id: {} ", id);
        return user;
    }

    public User updateUserById(Long id, UserUpdateRequest userUpdateRequest) {
        User user = findOrThrow(userRepository, id, "User");
        user.setActive(userUpdateRequest.active());
        User saved = userRepository.save(user);
        log.info("Updated user with id: {} ", id);
        return saved;
    }

    public void deleteUserById(Long id) {
        findOrThrow(userRepository, id, "User");
        userRepository.deleteById(id);
        log.info("Deleted user with id: {} ", id);
    }

}
