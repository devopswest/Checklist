package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Workflow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Workflow entity.
 */
public interface WorkflowSearchRepository extends ElasticsearchRepository<Workflow, Long> {
}
