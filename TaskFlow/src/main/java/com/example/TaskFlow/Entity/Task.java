package com.example.TaskFlow.Entity;

import com.example.TaskFlow.Enum.TaskPriority;
import com.example.TaskFlow.Enum.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Column
    private LocalDateTime deadline;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "assigned_user_id", nullable = false)
    private User assignedUser;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @JsonProperty("userId")
    public Long getUserId() {
        return assignedUser != null ? assignedUser.getId() : null;
    }

    @JsonProperty("projectId")
    public Long getProjectId() {
        return project != null ? project.getId() : null;
    }

    @JsonProperty("projectId")
    public void setProjectId(Long projectId) {
        if (projectId != null) {
            this.project = new Project();
            this.project.setId(projectId);
        }
    }

    @JsonProperty("userId")
    public void setUserId(Long userId) {
        if (userId != null) {
            this.assignedUser = new User();
            this.assignedUser.setId(userId);
        }
    }

}
