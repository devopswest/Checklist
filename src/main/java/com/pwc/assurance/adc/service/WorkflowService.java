package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.WorkflowDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Workflow.
 */
public interface WorkflowService {

    /**
     * Save a workflow.
     *
     * @param workflowDTO the entity to save
     * @return the persisted entity
     */
    WorkflowDTO save(WorkflowDTO workflowDTO);

    /**
     *  Get all the workflows.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WorkflowDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" workflow.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WorkflowDTO findOne(Long id);

    /**
     *  Delete the "id" workflow.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the workflow corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WorkflowDTO> search(String query, Pageable pageable);
}
