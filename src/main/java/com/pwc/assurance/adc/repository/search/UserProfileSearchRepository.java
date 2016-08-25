package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.UserProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserProfile entity.
 */
public interface UserProfileSearchRepository extends ElasticsearchRepository<UserProfile, Long> {
}
