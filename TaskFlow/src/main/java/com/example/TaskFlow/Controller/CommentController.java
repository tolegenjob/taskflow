package com.example.TaskFlow.Controller;


import com.example.TaskFlow.DTO.Request.CommentCreateRequest;
import com.example.TaskFlow.DTO.Request.CommentUpdateRequest;
import com.example.TaskFlow.DTO.Response.CommentCreateResponse;
import com.example.TaskFlow.DTO.Response.CommentResponse;
import com.example.TaskFlow.DTO.Response.CommentUpdateResponse;
import com.example.TaskFlow.Security.AccessService;
import com.example.TaskFlow.Service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final AccessService accessService;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId,
            @RequestBody @Valid CommentCreateRequest commentCreateRequest
    ) {
        if (!accessService.canCreateCommentGetAllCommentsByTaskId(userDetails, taskId)) {
            return ResponseEntity.status(403).build();
        }
        CommentCreateResponse commentCreateResponse = CommentCreateResponse.toDTO(commentService.createComment(taskId, commentCreateRequest));
        return ResponseEntity.ok(commentCreateResponse);
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getAllCommentsByTaskId(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (!accessService.canCreateCommentGetAllCommentsByTaskId(userDetails, taskId)) {
            return ResponseEntity.status(403).build();
        }
        Page<CommentResponse> commentResponses = commentService.getAllCommentsByTaskId(taskId, pageable)
                .map(CommentResponse::toDTO);
        return ResponseEntity.ok(commentResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId,
            @PathVariable Long id
    ) {
        if (!accessService.canGetComment(userDetails, id)) {
            return ResponseEntity.status(403).build();
        }
        CommentResponse commentResponse = CommentResponse.toDTO(commentService.getCommentById(taskId, id));
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentUpdateResponse> updateCommentById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId,
            @PathVariable Long id,
            @RequestBody @Valid CommentUpdateRequest commentUpdateRequest
    ) {
        if (!accessService.canUpdateDeleteComment(userDetails, id)) {
            return ResponseEntity.status(403).build();
        }
        CommentUpdateResponse commentUpdateResponse = CommentUpdateResponse.toDTO(commentService.updateCommentById(taskId, id, commentUpdateRequest));
        return ResponseEntity.ok(commentUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponse> deleteCommentById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId,
            @PathVariable Long id
    ) {
        if (!accessService.canUpdateDeleteComment(userDetails, id)) {
            return ResponseEntity.status(403).build();
        }
        commentService.deleteCommentById(taskId, id);
        return ResponseEntity.noContent().build();
    }

}
