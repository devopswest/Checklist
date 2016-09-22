package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ChecklistWorkflow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChecklistWorkflow entity.
 */
public interface ChecklistWorkflowSearchRepository extends ElasticsearchRepository<ChecklistWorkflow, Long> {
}
