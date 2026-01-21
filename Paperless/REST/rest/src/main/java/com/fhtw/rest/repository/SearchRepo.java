package com.fhtw.rest.repository;

import com.fhtw.rest.model.IndexedDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchRepo extends ElasticsearchRepository<IndexedDocument, Long> {
    List<IndexedDocument> findByContent(String content);

}
