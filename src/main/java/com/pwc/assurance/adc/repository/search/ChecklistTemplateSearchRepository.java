package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ChecklistTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChecklistTemplate entity.
 */
public interface ChecklistTemplateSearchRepository extends ElasticsearchRepository<ChecklistTemplate, Long> {
}
