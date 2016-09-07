package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Template;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Template entity.
 */
public interface TemplateSearchRepository extends ElasticsearchRepository<Template, Long> {
}
