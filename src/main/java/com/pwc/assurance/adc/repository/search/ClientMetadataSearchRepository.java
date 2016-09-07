package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ClientMetadata;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClientMetadata entity.
 */
public interface ClientMetadataSearchRepository extends ElasticsearchRepository<ClientMetadata, Long> {
}
