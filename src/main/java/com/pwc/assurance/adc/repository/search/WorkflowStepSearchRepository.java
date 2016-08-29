package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.WorkflowStep;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkflowStep entity.
 */
public interface WorkflowStepSearchRepository extends ElasticsearchRepository<WorkflowStep, Long> {
}
