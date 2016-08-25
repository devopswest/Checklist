package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.LicenseType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LicenseType entity.
 */
public interface LicenseTypeSearchRepository extends ElasticsearchRepository<LicenseType, Long> {
}
