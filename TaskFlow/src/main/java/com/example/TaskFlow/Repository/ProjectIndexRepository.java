package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Index.ProjectIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProjectIndexRepository extends ElasticsearchRepository<ProjectIndex, Long> {

    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["name^3", "description"],
              "fuzziness": "AUTO",
              "operator": "or"
            }
          }
        ]
      }
    }
    """)
    Page<ProjectIndex> searchByQuery(String query, Pageable pageable);

}
