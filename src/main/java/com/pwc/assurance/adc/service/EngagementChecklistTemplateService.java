package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.EngagementChecklistTemplateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing EngagementChecklistTemplate.
 */
public interface EngagementChecklistTemplateService {

    /**
     * Save a engagementChecklistTemplate.
     *
     * @param engagementChecklistTemplateDTO the entity to save
     * @return the persisted entity
     */
    EngagementChecklistTemplateDTO save(EngagementChecklistTemplateDTO engagementChecklistTemplateDTO);

    /**
     *  Get all the engagementChecklistTemplates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EngagementChecklistTemplateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" engagementChecklistTemplate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EngagementChecklistTemplateDTO findOne(Long id);

    /**
     *  Delete the "id" engagementChecklistTemplate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the engagementChecklistTemplate corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EngagementChecklistTemplateDTO> search(String query, Pageable pageable);
}
