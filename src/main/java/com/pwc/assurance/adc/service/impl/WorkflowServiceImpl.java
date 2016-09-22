package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.WorkflowService;
import com.pwc.assurance.adc.domain.Workflow;
import com.pwc.assurance.adc.repository.WorkflowRepository;
import com.pwc.assurance.adc.repository.search.WorkflowSearchRepository;
import com.pwc.assurance.adc.service.dto.WorkflowDTO;
import com.pwc.assurance.adc.service.mapper.WorkflowMapper;
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
 * Service Implementation for managing Workflow.
 */
@Service
@Transactional
public class WorkflowServiceImpl implements WorkflowService{

    private final Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);
    
    @Inject
    private WorkflowRepository workflowRepository;

    @Inject
    private WorkflowMapper workflowMapper;

    @Inject
    private WorkflowSearchRepository workflowSearchRepository;

    /**
     * Save a workflow.
     *
     * @param workflowDTO the entity to save
     * @return the persisted entity
     */
    public WorkflowDTO save(WorkflowDTO workflowDTO) {
        log.debug("Request to save Workflow : {}", workflowDTO);
        Workflow workflow = workflowMapper.workflowDTOToWorkflow(workflowDTO);
        workflow = workflowRepository.save(workflow);
        WorkflowDTO result = workflowMapper.workflowToWorkflowDTO(workflow);
        workflowSearchRepository.save(workflow);
        return result;
    }

    /**
     *  Get all the workflows.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<WorkflowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workflows");
        Page<Workflow> result = workflowRepository.findAll(pageable);
        return result.map(workflow -> workflowMapper.workflowToWorkflowDTO(workflow));
    }

    /**
     *  Get one workflow by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WorkflowDTO findOne(Long id) {
        log.debug("Request to get Workflow : {}", id);
        Workflow workflow = workflowRepository.findOne(id);
        WorkflowDTO workflowDTO = workflowMapper.workflowToWorkflowDTO(workflow);
        return workflowDTO;
    }

    /**
     *  Delete the  workflow by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Workflow : {}", id);
        workflowRepository.delete(id);
        workflowSearchRepository.delete(id);
    }

    /**
     * Search for the workflow corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkflowDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Workflows for query {}", query);
        Page<Workflow> result = workflowSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(workflow -> workflowMapper.workflowToWorkflowDTO(workflow));
    }
}
