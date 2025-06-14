package com.example.TaskFlow.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.TaskFlow.DTO.Event.Event;
import com.example.TaskFlow.Index.CommentIndex;
import com.example.TaskFlow.Repository.CommentIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentIndexService {

    private final CommentIndexRepository commentIndexRepository;
    private final ElasticsearchClient elasticsearchClient;

    public Page<CommentIndex> processSearch(
            String query,
            Long taskId,
            Pageable pageable) {

        if (taskId != null) {
            return commentIndexRepository.searchByQueryAndTask(query, taskId.toString(), pageable);
        } else {
            return commentIndexRepository.searchByQuery(query, pageable);
        }

    }

    public Map<String, Long> getCommentCountByUser() {
        try {
            SearchResponse<Void> response = elasticsearchClient.search(s -> s
                            .index("comments")
                            .size(0)
                            .aggregations("by_user", a -> a
                                    .terms(t -> t.field("userId"))
                            ),
                    Void.class
            );
            log.info("Got count of comments by user");
            return response.aggregations()
                    .get("by_user")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .collect(Collectors.toMap(
                            bucket -> bucket.key().stringValue(),
                            bucket -> bucket.docCount()
                    ));

        } catch (IOException e) {
            log.error("Error aggregating comment count by user: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public void createCommentIndex(Event event) {
        CommentIndex commentIndex = new CommentIndex();
        setProperties(event, commentIndex);
        commentIndexRepository.save(commentIndex);
        log.info("Comment index created: {}", commentIndex);
    }

    public void updateCommentIndex(Event event) {
        Long id = ((Number) event.payload().get("id")).longValue();
        CommentIndex commentIndex = findOrThrow(commentIndexRepository, id, "CommentIndex");
        setProperties(event, commentIndex);
        commentIndexRepository.save(commentIndex);
        log.info("Comment index updated: {}", commentIndex);
    }

    public void deleteCommentIndex(Event event) {
        Long id = ((Number) event.payload().get("id")).longValue();
        CommentIndex commentIndex = findOrThrow(commentIndexRepository, id, "CommentIndex");
        commentIndexRepository.delete(commentIndex);
        log.info("Comment index deleted: {}", commentIndex);
    }

    private void setProperties(Event event, CommentIndex commentIndex) {
        commentIndex.setId(((Number) event.payload().get("id")).longValue());
        commentIndex.setContent((String) event.payload().get("content"));
        commentIndex.setTaskId(String.valueOf(event.payload().get("taskId")));
        commentIndex.setUserId(String.valueOf(event.payload().get("userId")));
        commentIndex.setCreatedAt(LocalDateTime.parse((String) event.payload().get("createdAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
    }

}
