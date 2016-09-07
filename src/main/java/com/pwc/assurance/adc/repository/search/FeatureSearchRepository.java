package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Feature;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Feature entity.
 */
public interface FeatureSearchRepository extends ElasticsearchRepository<Feature, Long> {
}
