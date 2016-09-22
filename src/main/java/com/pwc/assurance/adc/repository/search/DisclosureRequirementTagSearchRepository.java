package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.DisclosureRequirementTag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DisclosureRequirementTag entity.
 */
public interface DisclosureRequirementTagSearchRepository extends ElasticsearchRepository<DisclosureRequirementTag, Long> {
}
