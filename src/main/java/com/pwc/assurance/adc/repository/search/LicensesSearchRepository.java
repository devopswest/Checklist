package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Licenses;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Licenses entity.
 */
public interface LicensesSearchRepository extends ElasticsearchRepository<Licenses, Long> {
}
