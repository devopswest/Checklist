package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.EngagementChecklistTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EngagementChecklistTemplate entity.
 */
public interface EngagementChecklistTemplateSearchRepository extends ElasticsearchRepository<EngagementChecklistTemplate, Long> {
}
