package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.NotificationService;
import com.pwc.assurance.adc.domain.Notification;
import com.pwc.assurance.adc.repository.NotificationRepository;
import com.pwc.assurance.adc.repository.search.NotificationSearchRepository;
import com.pwc.assurance.adc.service.dto.NotificationDTO;
import com.pwc.assurance.adc.service.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Notification.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService{

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    
    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationMapper notificationMapper;

    @Inject
    private NotificationSearchRepository notificationSearchRepository;

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save
     * @return the persisted entity
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.notificationDTOToNotification(notificationDTO);
        notification = notificationRepository.save(notification);
        NotificationDTO result = notificationMapper.notificationToNotificationDTO(notification);
        notificationSearchRepository.save(notification);
        return result;
    }

    /**
     *  Get all the notifications.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        Page<Notification> result = notificationRepository.findAll(pageable);
        return result.map(notification -> notificationMapper.notificationToNotificationDTO(notification));
    }

    /**
     *  Get one notification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public NotificationDTO findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        NotificationDTO notificationDTO = notificationMapper.notificationToNotificationDTO(notification);
        return notificationDTO;
    }

    /**
     *  Delete the  notification by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.delete(id);
        notificationSearchRepository.delete(id);
    }

    /**
     * Search for the notification corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notifications for query {}", query);
        Page<Notification> result = notificationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(notification -> notificationMapper.notificationToNotificationDTO(notification));
    }
}
