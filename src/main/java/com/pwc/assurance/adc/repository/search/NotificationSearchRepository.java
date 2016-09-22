package com.pwc.assurance.adc.repository.search;

import com.pwc.assurance.adc.domain.Notification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Notification entity.
 */
public interface NotificationSearchRepository extends ElasticsearchRepository<Notification, Long> {
}
