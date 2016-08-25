package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.AuditQuestionResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AuditQuestionResponse entity.
 */
public interface AuditQuestionResponseSearchRepository extends ElasticsearchRepository<AuditQuestionResponse, Long> {
}
