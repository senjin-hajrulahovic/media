package com.hardcodedlambda.media.elasticsearch;

import com.hardcodedlambda.media.model.Media;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MediaElasticSearchRepository extends ElasticsearchRepository<Media, Long> { }
