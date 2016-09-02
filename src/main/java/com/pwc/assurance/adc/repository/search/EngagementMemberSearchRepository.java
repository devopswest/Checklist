package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.EngagementMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EngagementMember entity.
 */
public interface EngagementMemberSearchRepository extends ElasticsearchRepository<EngagementMember, Long> {
}
