package com.example.TaskFlow.Repository;

import com.example.TaskFlow.Index.TaskIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TaskIndexRepository extends ElasticsearchRepository<TaskIndex, Long> {

    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["title^3", "description"],
              "fuzziness": "AUTO",
              "operator": "or"
            }
          }
        ],
        "filter": [
          {
            "term": {
              "status": "?1"
            }
          },
          {
            "term": {
              "priority": "?2"
            }
          }
        ]
      }
    }
    """)
    Page<TaskIndex> searchByQueryAndStatusAndPriority(String query, String status, String priority, Pageable pageable);


    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["title^3", "description"],
              "fuzziness": "AUTO",
              "operator": "or"
            }
          }
        ],
        "filter": [
          {
            "term": {
              "status": "?1"
            }
          }
        ]
      }
    }
    """)
    Page<TaskIndex> searchByQueryAndStatus(String query, String status, Pageable pageable);


    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["title^3", "description"],
              "fuzziness": "AUTO",
              "operator": "or"
            }
          }
        ],
        "filter": [
          {
            "term": {
              "priority": "?1"
            }
          }
        ]
      }
    }
    """)
    Page<TaskIndex> searchByQueryAndPriority(String query, String priority, Pageable pageable);


    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["title^3", "description"],
              "fuzziness": "AUTO",
              "operator": "or"
            }
          }
        ]
      }
    }
    """)
    Page<TaskIndex> searchByQuery(String query, Pageable pageable);
}
