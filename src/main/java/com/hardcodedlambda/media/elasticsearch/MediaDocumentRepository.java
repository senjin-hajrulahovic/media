package com.hardcodedlambda.media.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MediaDocumentRepository extends ElasticsearchRepository<MediaDocument, String> { }
