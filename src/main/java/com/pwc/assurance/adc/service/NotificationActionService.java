package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.NotificationActionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing NotificationAction.
 */
public interface NotificationActionService {

    /**
     * Save a notificationAction.
     *
     * @param notificationActionDTO the entity to save
     * @return the persisted entity
     */
    NotificationActionDTO save(NotificationActionDTO notificationActionDTO);

    /**
     *  Get all the notificationActions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NotificationActionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" notificationAction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NotificationActionDTO findOne(Long id);

    /**
     *  Delete the "id" notificationAction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the notificationAction corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NotificationActionDTO> search(String query, Pageable pageable);
}
