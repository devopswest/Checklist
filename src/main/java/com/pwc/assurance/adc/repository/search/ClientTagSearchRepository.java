package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ClientTag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClientTag entity.
 */
public interface ClientTagSearchRepository extends ElasticsearchRepository<ClientTag, Long> {
}
