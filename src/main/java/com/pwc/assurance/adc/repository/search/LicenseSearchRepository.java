package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.License;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the License entity.
 */
public interface LicenseSearchRepository extends ElasticsearchRepository<License, Long> {
}
