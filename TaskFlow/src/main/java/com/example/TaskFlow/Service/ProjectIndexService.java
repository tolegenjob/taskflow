package com.example.TaskFlow.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.TaskFlow.DTO.Event.Event;
import com.example.TaskFlow.Enum.ProjectStatus;
import com.example.TaskFlow.Index.ProjectIndex;
import com.example.TaskFlow.Repository.ProjectIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.TaskFlow.Util.EntityUtil.findOrThrow;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectIndexService {

    private final ProjectIndexRepository projectIndexRepository;
    private final ElasticsearchClient elasticsearchClient;

    public Page<ProjectIndex> processSearch(
            String query,
            Pageable pageable) {

        return projectIndexRepository.searchByQuery(query, pageable);

    }

    public Map<String, Long> getProjectCountByStatus() {
        try {
            SearchResponse<Void> response = elasticsearchClient.search(s -> s
                            .index("projects")
                            .size(0)
                            .aggregations("by_status", a -> a
                                    .terms(t -> t.field("status"))
                            ),
                    Void.class
            );
            log.info("Got count of projects by status");
            return response.aggregations()
                    .get("by_status")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .collect(Collectors.toMap(
                            bucket -> bucket.key().stringValue(),
                            bucket -> bucket.docCount()
                    ));

        } catch (IOException e) {
            log.error("Error aggregating project count by status: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public void createProjectIndex(Event event) {
        ProjectIndex projectIndex = new ProjectIndex();
        setProperties(event, projectIndex);
        projectIndexRepository.save(projectIndex);
        log.info("Project index created: {}", projectIndex);
    }

    public void updateProjectIndex(Event event) {
        Long id = ((Number) event.payload().get("id")).longValue();
        ProjectIndex projectIndex = findOrThrow(projectIndexRepository, id, "ProjectIndex");
        setProperties(event, projectIndex);
        projectIndexRepository.save(projectIndex);
        log.info("Project index updated: {}", projectIndex);
    }

    public void deleteProjectIndex(Event event) {
        Long id = ((Number) event.payload().get("id")).longValue();
        ProjectIndex projectIndex = findOrThrow(projectIndexRepository, id, "ProjectIndex");
        projectIndexRepository.delete(projectIndex);
        log.info("Project index deleted: {}", projectIndex);
    }

    private void setProperties(Event event, ProjectIndex projectIndex) {
        projectIndex.setId(((Number) event.payload().get("id")).longValue());
        projectIndex.setName((String) event.payload().get("name"));
        projectIndex.setDescription((String) event.payload().get("description"));
        projectIndex.setStatus(ProjectStatus.valueOf((String) event.payload().get("status")));
    }

}
