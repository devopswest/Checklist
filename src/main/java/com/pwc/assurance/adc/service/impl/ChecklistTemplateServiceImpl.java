package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.ChecklistTemplateService;
import com.pwc.assurance.adc.domain.ChecklistTemplate;
import com.pwc.assurance.adc.repository.ChecklistTemplateRepository;
import com.pwc.assurance.adc.repository.search.ChecklistTemplateSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistTemplateDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistTemplateMapper;
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
 * Service Implementation for managing ChecklistTemplate.
 */
@Service
@Transactional
public class ChecklistTemplateServiceImpl implements ChecklistTemplateService{

    private final Logger log = LoggerFactory.getLogger(ChecklistTemplateServiceImpl.class);
    
    @Inject
    private ChecklistTemplateRepository checklistTemplateRepository;

    @Inject
    private ChecklistTemplateMapper checklistTemplateMapper;

    @Inject
    private ChecklistTemplateSearchRepository checklistTemplateSearchRepository;

    /**
     * Save a checklistTemplate.
     *
     * @param checklistTemplateDTO the entity to save
     * @return the persisted entity
     */
    public ChecklistTemplateDTO save(ChecklistTemplateDTO checklistTemplateDTO) {
        log.debug("Request to save ChecklistTemplate : {}", checklistTemplateDTO);
        ChecklistTemplate checklistTemplate = checklistTemplateMapper.checklistTemplateDTOToChecklistTemplate(checklistTemplateDTO);
        checklistTemplate = checklistTemplateRepository.save(checklistTemplate);
        ChecklistTemplateDTO result = checklistTemplateMapper.checklistTemplateToChecklistTemplateDTO(checklistTemplate);
        checklistTemplateSearchRepository.save(checklistTemplate);
        return result;
    }

    /**
     *  Get all the checklistTemplates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChecklistTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChecklistTemplates");
        Page<ChecklistTemplate> result = checklistTemplateRepository.findAll(pageable);
        return result.map(checklistTemplate -> checklistTemplateMapper.checklistTemplateToChecklistTemplateDTO(checklistTemplate));
    }

    /**
     *  Get one checklistTemplate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ChecklistTemplateDTO findOne(Long id) {
        log.debug("Request to get ChecklistTemplate : {}", id);
        ChecklistTemplate checklistTemplate = checklistTemplateRepository.findOne(id);
        ChecklistTemplateDTO checklistTemplateDTO = checklistTemplateMapper.checklistTemplateToChecklistTemplateDTO(checklistTemplate);
        return checklistTemplateDTO;
    }

    /**
     *  Delete the  checklistTemplate by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ChecklistTemplate : {}", id);
        checklistTemplateRepository.delete(id);
        checklistTemplateSearchRepository.delete(id);
    }

    /**
     * Search for the checklistTemplate corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChecklistTemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChecklistTemplates for query {}", query);
        Page<ChecklistTemplate> result = checklistTemplateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(checklistTemplate -> checklistTemplateMapper.checklistTemplateToChecklistTemplateDTO(checklistTemplate));
    }
}
