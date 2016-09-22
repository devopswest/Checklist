package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.WorkflowStepService;
import com.pwc.assurance.adc.domain.WorkflowStep;
import com.pwc.assurance.adc.repository.WorkflowStepRepository;
import com.pwc.assurance.adc.repository.search.WorkflowStepSearchRepository;
import com.pwc.assurance.adc.service.dto.WorkflowStepDTO;
import com.pwc.assurance.adc.service.mapper.WorkflowStepMapper;
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
 * Service Implementation for managing WorkflowStep.
 */
@Service
@Transactional
public class WorkflowStepServiceImpl implements WorkflowStepService{

    private final Logger log = LoggerFactory.getLogger(WorkflowStepServiceImpl.class);
    
    @Inject
    private WorkflowStepRepository workflowStepRepository;

    @Inject
    private WorkflowStepMapper workflowStepMapper;

    @Inject
    private WorkflowStepSearchRepository workflowStepSearchRepository;

    /**
     * Save a workflowStep.
     *
     * @param workflowStepDTO the entity to save
     * @return the persisted entity
     */
    public WorkflowStepDTO save(WorkflowStepDTO workflowStepDTO) {
        log.debug("Request to save WorkflowStep : {}", workflowStepDTO);
        WorkflowStep workflowStep = workflowStepMapper.workflowStepDTOToWorkflowStep(workflowStepDTO);
        workflowStep = workflowStepRepository.save(workflowStep);
        WorkflowStepDTO result = workflowStepMapper.workflowStepToWorkflowStepDTO(workflowStep);
        workflowStepSearchRepository.save(workflowStep);
        return result;
    }

    /**
     *  Get all the workflowSteps.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<WorkflowStepDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkflowSteps");
        Page<WorkflowStep> result = workflowStepRepository.findAll(pageable);
        return result.map(workflowStep -> workflowStepMapper.workflowStepToWorkflowStepDTO(workflowStep));
    }

    /**
     *  Get one workflowStep by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WorkflowStepDTO findOne(Long id) {
        log.debug("Request to get WorkflowStep : {}", id);
        WorkflowStep workflowStep = workflowStepRepository.findOne(id);
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.workflowStepToWorkflowStepDTO(workflowStep);
        return workflowStepDTO;
    }

    /**
     *  Delete the  workflowStep by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkflowStep : {}", id);
        workflowStepRepository.delete(id);
        workflowStepSearchRepository.delete(id);
    }

    /**
     * Search for the workflowStep corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkflowStepDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkflowSteps for query {}", query);
        Page<WorkflowStep> result = workflowStepSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(workflowStep -> workflowStepMapper.workflowStepToWorkflowStepDTO(workflowStep));
    }
}
