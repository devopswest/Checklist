package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.NotificationAction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NotificationAction entity.
 */
public interface NotificationActionSearchRepository extends ElasticsearchRepository<NotificationAction, Long> {
}
