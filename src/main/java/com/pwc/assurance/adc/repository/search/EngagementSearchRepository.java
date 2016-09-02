package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Engagement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Engagement entity.
 */
public interface EngagementSearchRepository extends ElasticsearchRepository<Engagement, Long> {
}
