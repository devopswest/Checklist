package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.DisclosureRequirement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DisclosureRequirement entity.
 */
public interface DisclosureRequirementSearchRepository extends ElasticsearchRepository<DisclosureRequirement, Long> {
}
