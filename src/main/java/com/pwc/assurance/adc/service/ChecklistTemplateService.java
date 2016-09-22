package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ChecklistTemplateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ChecklistTemplate.
 */
public interface ChecklistTemplateService {

    /**
     * Save a checklistTemplate.
     *
     * @param checklistTemplateDTO the entity to save
     * @return the persisted entity
     */
    ChecklistTemplateDTO save(ChecklistTemplateDTO checklistTemplateDTO);

    /**
     *  Get all the checklistTemplates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistTemplateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" checklistTemplate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChecklistTemplateDTO findOne(Long id);

    /**
     *  Delete the "id" checklistTemplate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the checklistTemplate corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistTemplateDTO> search(String query, Pageable pageable);
}
