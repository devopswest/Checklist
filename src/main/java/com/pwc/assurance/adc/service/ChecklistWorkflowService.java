package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ChecklistWorkflowDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ChecklistWorkflow.
 */
public interface ChecklistWorkflowService {

    /**
     * Save a checklistWorkflow.
     *
     * @param checklistWorkflowDTO the entity to save
     * @return the persisted entity
     */
    ChecklistWorkflowDTO save(ChecklistWorkflowDTO checklistWorkflowDTO);

    /**
     *  Get all the checklistWorkflows.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistWorkflowDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" checklistWorkflow.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChecklistWorkflowDTO findOne(Long id);

    /**
     *  Delete the "id" checklistWorkflow.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the checklistWorkflow corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistWorkflowDTO> search(String query, Pageable pageable);
}
