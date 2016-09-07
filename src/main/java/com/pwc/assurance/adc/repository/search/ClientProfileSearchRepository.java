package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ClientProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClientProfile entity.
 */
public interface ClientProfileSearchRepository extends ElasticsearchRepository<ClientProfile, Long> {
}
