package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.GlobalConfiguration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GlobalConfiguration entity.
 */
public interface GlobalConfigurationSearchRepository extends ElasticsearchRepository<GlobalConfiguration, Long> {
}
