package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.WorkflowStepDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing WorkflowStep.
 */
public interface WorkflowStepService {

    /**
     * Save a workflowStep.
     *
     * @param workflowStepDTO the entity to save
     * @return the persisted entity
     */
    WorkflowStepDTO save(WorkflowStepDTO workflowStepDTO);

    /**
     *  Get all the workflowSteps.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WorkflowStepDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" workflowStep.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WorkflowStepDTO findOne(Long id);

    /**
     *  Delete the "id" workflowStep.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the workflowStep corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WorkflowStepDTO> search(String query, Pageable pageable);
}
