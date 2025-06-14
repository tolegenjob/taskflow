package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Index.CommentIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentIndexRepository extends ElasticsearchRepository<CommentIndex, Long> {

    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["content"],
              "fuzziness": "AUTO",
              "operator": "or"
            }
          }
        ],
        "filter": [
          {
            "term": {
              "taskId": "?1"
            }
          }
        ]
      }
    }
    """)
    Page<CommentIndex> searchByQueryAndTask(String query, String task, Pageable pageable);


    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["content"],
              "fuzziness": "AUTO",
              "operator": "or"
            }
          }
        ]
      }
    }
    """)
    Page<CommentIndex> searchByQuery(String query, Pageable pageable);


}
