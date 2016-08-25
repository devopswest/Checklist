package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ChecklistQuestion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChecklistQuestion entity.
 */
public interface ChecklistQuestionSearchRepository extends ElasticsearchRepository<ChecklistQuestion, Long> {
}
