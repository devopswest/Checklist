package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.AuditProfileLogEntry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AuditProfileLogEntry entity.
 */
public interface AuditProfileLogEntrySearchRepository extends ElasticsearchRepository<AuditProfileLogEntry, Long> {
}
