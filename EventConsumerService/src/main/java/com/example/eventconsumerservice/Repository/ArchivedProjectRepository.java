package com.example.eventconsumerservice.Repository;

import com.example.eventconsumerservice.Entity.ArchivedProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedProjectRepository extends JpaRepository<ArchivedProject, Long> {

    @NonNull
    Page<ArchivedProject> findAll(@NonNull Pageable pageable);

}
