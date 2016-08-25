package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.AuditProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AuditProfile entity.
 */
public interface AuditProfileSearchRepository extends ElasticsearchRepository<AuditProfile, Long> {
}
