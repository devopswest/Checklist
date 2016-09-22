package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ChecklistAnswer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChecklistAnswer entity.
 */
public interface ChecklistAnswerSearchRepository extends ElasticsearchRepository<ChecklistAnswer, Long> {
}
