package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Taxonomy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Taxonomy entity.
 */
public interface TaxonomySearchRepository extends ElasticsearchRepository<Taxonomy, Long> {
}
