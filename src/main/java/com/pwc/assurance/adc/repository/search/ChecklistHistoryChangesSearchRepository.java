package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ChecklistHistoryChanges;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChecklistHistoryChanges entity.
 */
public interface ChecklistHistoryChangesSearchRepository extends ElasticsearchRepository<ChecklistHistoryChanges, Long> {
}
