package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.NotificationActionService;
import com.pwc.assurance.adc.domain.NotificationAction;
import com.pwc.assurance.adc.repository.NotificationActionRepository;
import com.pwc.assurance.adc.repository.search.NotificationActionSearchRepository;
import com.pwc.assurance.adc.service.dto.NotificationActionDTO;
import com.pwc.assurance.adc.service.mapper.NotificationActionMapper;
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
 * Service Implementation for managing NotificationAction.
 */
@Service
@Transactional
public class NotificationActionServiceImpl implements NotificationActionService{

    private final Logger log = LoggerFactory.getLogger(NotificationActionServiceImpl.class);
    
    @Inject
    private NotificationActionRepository notificationActionRepository;

    @Inject
    private NotificationActionMapper notificationActionMapper;

    @Inject
    private NotificationActionSearchRepository notificationActionSearchRepository;

    /**
     * Save a notificationAction.
     *
     * @param notificationActionDTO the entity to save
     * @return the persisted entity
     */
    public NotificationActionDTO save(NotificationActionDTO notificationActionDTO) {
        log.debug("Request to save NotificationAction : {}", notificationActionDTO);
        NotificationAction notificationAction = notificationActionMapper.notificationActionDTOToNotificationAction(notificationActionDTO);
        notificationAction = notificationActionRepository.save(notificationAction);
        NotificationActionDTO result = notificationActionMapper.notificationActionToNotificationActionDTO(notificationAction);
        notificationActionSearchRepository.save(notificationAction);
        return result;
    }

    /**
     *  Get all the notificationActions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<NotificationActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NotificationActions");
        Page<NotificationAction> result = notificationActionRepository.findAll(pageable);
        return result.map(notificationAction -> notificationActionMapper.notificationActionToNotificationActionDTO(notificationAction));
    }

    /**
     *  Get one notificationAction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public NotificationActionDTO findOne(Long id) {
        log.debug("Request to get NotificationAction : {}", id);
        NotificationAction notificationAction = notificationActionRepository.findOne(id);
        NotificationActionDTO notificationActionDTO = notificationActionMapper.notificationActionToNotificationActionDTO(notificationAction);
        return notificationActionDTO;
    }

    /**
     *  Delete the  notificationAction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NotificationAction : {}", id);
        notificationActionRepository.delete(id);
        notificationActionSearchRepository.delete(id);
    }

    /**
     * Search for the notificationAction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationActionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NotificationActions for query {}", query);
        Page<NotificationAction> result = notificationActionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(notificationAction -> notificationActionMapper.notificationActionToNotificationActionDTO(notificationAction));
    }
}
