package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.ChecklistWorkflowService;
import com.pwc.assurance.adc.domain.ChecklistWorkflow;
import com.pwc.assurance.adc.repository.ChecklistWorkflowRepository;
import com.pwc.assurance.adc.repository.search.ChecklistWorkflowSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistWorkflowDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistWorkflowMapper;
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
 * Service Implementation for managing ChecklistWorkflow.
 */
@Service
@Transactional
public class ChecklistWorkflowServiceImpl implements ChecklistWorkflowService{

    private final Logger log = LoggerFactory.getLogger(ChecklistWorkflowServiceImpl.class);
    
    @Inject
    private ChecklistWorkflowRepository checklistWorkflowRepository;

    @Inject
    private ChecklistWorkflowMapper checklistWorkflowMapper;

    @Inject
    private ChecklistWorkflowSearchRepository checklistWorkflowSearchRepository;

    /**
     * Save a checklistWorkflow.
     *
     * @param checklistWorkflowDTO the entity to save
     * @return the persisted entity
     */
    public ChecklistWorkflowDTO save(ChecklistWorkflowDTO checklistWorkflowDTO) {
        log.debug("Request to save ChecklistWorkflow : {}", checklistWorkflowDTO);
        ChecklistWorkflow checklistWorkflow = checklistWorkflowMapper.checklistWorkflowDTOToChecklistWorkflow(checklistWorkflowDTO);
        checklistWorkflow = checklistWorkflowRepository.save(checklistWorkflow);
        ChecklistWorkflowDTO result = checklistWorkflowMapper.checklistWorkflowToChecklistWorkflowDTO(checklistWorkflow);
        checklistWorkflowSearchRepository.save(checklistWorkflow);
        return result;
    }

    /**
     *  Get all the checklistWorkflows.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChecklistWorkflowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChecklistWorkflows");
        Page<ChecklistWorkflow> result = checklistWorkflowRepository.findAll(pageable);
        return result.map(checklistWorkflow -> checklistWorkflowMapper.checklistWorkflowToChecklistWorkflowDTO(checklistWorkflow));
    }

    /**
     *  Get one checklistWorkflow by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ChecklistWorkflowDTO findOne(Long id) {
        log.debug("Request to get ChecklistWorkflow : {}", id);
        ChecklistWorkflow checklistWorkflow = checklistWorkflowRepository.findOne(id);
        ChecklistWorkflowDTO checklistWorkflowDTO = checklistWorkflowMapper.checklistWorkflowToChecklistWorkflowDTO(checklistWorkflow);
        return checklistWorkflowDTO;
    }

    /**
     *  Delete the  checklistWorkflow by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ChecklistWorkflow : {}", id);
        checklistWorkflowRepository.delete(id);
        checklistWorkflowSearchRepository.delete(id);
    }

    /**
     * Search for the checklistWorkflow corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChecklistWorkflowDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChecklistWorkflows for query {}", query);
        Page<ChecklistWorkflow> result = checklistWorkflowSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(checklistWorkflow -> checklistWorkflowMapper.checklistWorkflowToChecklistWorkflowDTO(checklistWorkflow));
    }
}
