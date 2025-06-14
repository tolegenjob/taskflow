package com.example.TaskFlow.Migration;

import com.example.TaskFlow.Entity.Comment;
import com.example.TaskFlow.Index.CommentIndex;
import com.example.TaskFlow.Repository.CommentIndexRepository;
import com.example.TaskFlow.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsToElasticMigrationService {

    private final CommentRepository commentRepository;
    private final CommentIndexRepository commentIndexRepository;

    @Transactional(readOnly = true)
    public void migrateAllCommentsToElasticsearch() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentIndex> commentIndicesToSave = comments.stream()
                .filter(comment -> !commentIndexRepository.existsById(comment.getId()))
                .map(task -> {
                    CommentIndex commentIndex = new CommentIndex();
                    setProperties(task, commentIndex);
                    return commentIndex;
                })
                .toList();
        if (!commentIndicesToSave.isEmpty()) {
            commentIndexRepository.saveAll(commentIndicesToSave);
        }
    }

    private void setProperties(Comment comment, CommentIndex commentIndex) {
        commentIndex.setId(comment.getId());
        commentIndex.setContent(comment.getContent());
        commentIndex.setUserId(String.valueOf(comment.getUser().getId()));
        commentIndex.setTaskId(String.valueOf(comment.getTask().getId()));
        commentIndex.setCreatedAt(comment.getCreatedAt());
    }

}
