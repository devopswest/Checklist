package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Checklist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Checklist entity.
 */
public interface ChecklistSearchRepository extends ElasticsearchRepository<Checklist, Long> {
}
