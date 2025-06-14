package com.example.TaskFlow.Security;

import com.example.TaskFlow.DTO.Request.TaskCreateRequest;
import com.example.TaskFlow.Entity.Comment;
import com.example.TaskFlow.Entity.Project;
import com.example.TaskFlow.Entity.Task;
import com.example.TaskFlow.Entity.User;
import com.example.TaskFlow.Repository.CommentRepository;
import com.example.TaskFlow.Repository.ProjectRepository;
import com.example.TaskFlow.Repository.TaskRepository;
import com.example.TaskFlow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;
import static com.example.TaskFlow.Util.EntityUtil.findUserByEmailOrThrow;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public boolean canGetTask(UserDetails userDetails, Long taskId) {
        User currentUser = findUserByEmailOrThrow(userRepository, userDetails.getUsername());
        Task task = findOrThrow(taskRepository, taskId, "Task");
        return switch (currentUser.getRole()) {
            case ADMIN, SUPERVISOR  -> true;
            case PROJECT_MANAGER -> task.getProject().getOwner().equals(currentUser);
            case USER -> task.getAssignedUser().equals(currentUser);
        };
    }

    public boolean canUpdateDeleteTask(UserDetails userDetails, Long taskId) {
        User currentUser = findUserByEmailOrThrow(userRepository, userDetails.getUsername());
        Task task = findOrThrow(taskRepository, taskId, "Task");
        return switch (currentUser.getRole()) {
            case ADMIN -> true;
            case PROJECT_MANAGER -> task.getProject().getOwner().equals(currentUser);
            default -> false;
        };
    }

    public boolean canCreateTask(UserDetails userDetails, TaskCreateRequest createRequest) {
        User currentUser = findUserByEmailOrThrow(userRepository, userDetails.getUsername());
        Project project = findOrThrow(projectRepository, createRequest.projectId(), "Project");
        return switch (currentUser.getRole()) {
            case ADMIN -> true;
            case PROJECT_MANAGER -> project.getOwner().equals(currentUser);
            default -> false;
        };
    }

    public boolean canGetProject(UserDetails userDetails, Long projectId) {
        User currentUser = findUserByEmailOrThrow(userRepository, userDetails.getUsername());
        Project project = findOrThrow(projectRepository, projectId, "Project");
        return switch (currentUser.getRole()) {
            case ADMIN, SUPERVISOR  -> true;
            case PROJECT_MANAGER -> project.getOwner().equals(currentUser);
            default -> false;
        };
    }

    public boolean canUpdateDeleteComment(UserDetails userDetails, Long commentId) {
        User currentUser = findUserByEmailOrThrow(userRepository, userDetails.getUsername());
        Comment comment = findOrThrow(commentRepository, commentId, "Comment");
        return switch (currentUser.getRole()) {
            case ADMIN, SUPERVISOR, PROJECT_MANAGER  -> true;
            case USER -> comment.getUser().equals(currentUser);
        };
    }

    public boolean canGetComment(UserDetails userDetails, Long commentId) {
        User currentUser = findUserByEmailOrThrow(userRepository, userDetails.getUsername());
        Comment comment = findOrThrow(commentRepository, commentId, "Comment");
        return switch (currentUser.getRole()) {
            case ADMIN, SUPERVISOR  -> true;
            case PROJECT_MANAGER -> comment.getTask().getProject().getOwner().equals(currentUser);
            case USER -> comment.getTask().getProject().getUsers().contains(currentUser);
        };
    }



    public boolean canCreateCommentGetAllCommentsByTaskId(UserDetails userDetails, Long taskId) {
        User currentUser = findUserByEmailOrThrow(userRepository, userDetails.getUsername());
        Task task = findOrThrow(taskRepository, taskId, "Task");
        return switch (currentUser.getRole()) {
            case ADMIN, SUPERVISOR  -> true;
            case PROJECT_MANAGER -> task.getProject().getOwner().equals(currentUser);
            case USER -> task.getProject().getUsers().contains(currentUser);
        };
    }

}
