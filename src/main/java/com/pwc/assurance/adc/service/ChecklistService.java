package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ChecklistDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Checklist.
 */
public interface ChecklistService {

    /**
     * Save a checklist.
     *
     * @param checklistDTO the entity to save
     * @return the persisted entity
     */
    ChecklistDTO save(ChecklistDTO checklistDTO);

    /**
     *  Get all the checklists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" checklist.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChecklistDTO findOne(Long id);

    /**
     *  Delete the "id" checklist.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the checklist corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistDTO> search(String query, Pageable pageable);
}
