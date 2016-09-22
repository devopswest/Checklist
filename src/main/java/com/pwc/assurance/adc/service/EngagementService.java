package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.EngagementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Engagement.
 */
public interface EngagementService {

    /**
     * Save a engagement.
     *
     * @param engagementDTO the entity to save
     * @return the persisted entity
     */
    EngagementDTO save(EngagementDTO engagementDTO);

    /**
     *  Get all the engagements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EngagementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" engagement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EngagementDTO findOne(Long id);

    /**
     *  Delete the "id" engagement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the engagement corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EngagementDTO> search(String query, Pageable pageable);
}
