package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"projects"})
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
