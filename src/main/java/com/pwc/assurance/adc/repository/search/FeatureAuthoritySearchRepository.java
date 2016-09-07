package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.FeatureAuthority;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FeatureAuthority entity.
 */
public interface FeatureAuthoritySearchRepository extends ElasticsearchRepository<FeatureAuthority, Long> {
}
