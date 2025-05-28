package com.example.TaskFlow.Controller;


import com.example.TaskFlow.DTO.Request.CommentCreateRequest;
import com.example.TaskFlow.DTO.Request.CommentUpdateRequest;
import com.example.TaskFlow.DTO.Response.CommentCreateResponse;
import com.example.TaskFlow.DTO.Response.CommentResponse;
import com.example.TaskFlow.DTO.Response.CommentUpdateResponse;
import com.example.TaskFlow.Service.CommentService;
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
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(@PathVariable Long taskId, @RequestBody @Valid CommentCreateRequest commentCreateRequest) {
        CommentCreateResponse commentCreateResponse = CommentCreateResponse.toDTO(commentService.createComment(taskId, commentCreateRequest));
        return ResponseEntity.ok(commentCreateResponse);
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getAllCommentsByTaskId(@PathVariable Long taskId, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponse> commentResponses = commentService.getAllCommentsByTaskId(taskId, pageable)
                .map(CommentResponse::toDTO);
        return ResponseEntity.ok(commentResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long taskId, @PathVariable Long id) {
        CommentResponse commentResponse = CommentResponse.toDTO(commentService.getCommentById(taskId, id));
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentUpdateResponse> updateCommentById(@PathVariable Long taskId, @PathVariable Long id, @RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {
        CommentUpdateResponse commentUpdateResponse = CommentUpdateResponse.toDTO(commentService.updateCommentById(taskId, id, commentUpdateRequest));
        return ResponseEntity.ok(commentUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponse> deleteCommentById(@PathVariable Long taskId, @PathVariable Long id) {
        commentService.deleteCommentById(taskId, id);
        return ResponseEntity.noContent().build();
    }

}
