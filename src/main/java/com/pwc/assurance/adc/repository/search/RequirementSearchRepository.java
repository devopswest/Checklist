package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Requirement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Requirement entity.
 */
public interface RequirementSearchRepository extends ElasticsearchRepository<Requirement, Long> {
}
