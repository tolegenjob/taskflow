package com.example.notificationservice.Repository;

import com.example.notificationservice.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @NonNull
    Page<Notification> findAll(@NonNull Pageable pageable);

}

