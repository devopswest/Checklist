package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.ClientLicense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClientLicense entity.
 */
public interface ClientLicenseSearchRepository extends ElasticsearchRepository<ClientLicense, Long> {
}
