package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.EngagementChecklistTemplateService;
import com.pwc.assurance.adc.domain.EngagementChecklistTemplate;
import com.pwc.assurance.adc.repository.EngagementChecklistTemplateRepository;
import com.pwc.assurance.adc.repository.search.EngagementChecklistTemplateSearchRepository;
import com.pwc.assurance.adc.service.dto.EngagementChecklistTemplateDTO;
import com.pwc.assurance.adc.service.mapper.EngagementChecklistTemplateMapper;
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
 * Service Implementation for managing EngagementChecklistTemplate.
 */
@Service
@Transactional
public class EngagementChecklistTemplateServiceImpl implements EngagementChecklistTemplateService{

    private final Logger log = LoggerFactory.getLogger(EngagementChecklistTemplateServiceImpl.class);
    
    @Inject
    private EngagementChecklistTemplateRepository engagementChecklistTemplateRepository;

    @Inject
    private EngagementChecklistTemplateMapper engagementChecklistTemplateMapper;

    @Inject
    private EngagementChecklistTemplateSearchRepository engagementChecklistTemplateSearchRepository;

    /**
     * Save a engagementChecklistTemplate.
     *
     * @param engagementChecklistTemplateDTO the entity to save
     * @return the persisted entity
     */
    public EngagementChecklistTemplateDTO save(EngagementChecklistTemplateDTO engagementChecklistTemplateDTO) {
        log.debug("Request to save EngagementChecklistTemplate : {}", engagementChecklistTemplateDTO);
        EngagementChecklistTemplate engagementChecklistTemplate = engagementChecklistTemplateMapper.engagementChecklistTemplateDTOToEngagementChecklistTemplate(engagementChecklistTemplateDTO);
        engagementChecklistTemplate = engagementChecklistTemplateRepository.save(engagementChecklistTemplate);
        EngagementChecklistTemplateDTO result = engagementChecklistTemplateMapper.engagementChecklistTemplateToEngagementChecklistTemplateDTO(engagementChecklistTemplate);
        engagementChecklistTemplateSearchRepository.save(engagementChecklistTemplate);
        return result;
    }

    /**
     *  Get all the engagementChecklistTemplates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<EngagementChecklistTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EngagementChecklistTemplates");
        Page<EngagementChecklistTemplate> result = engagementChecklistTemplateRepository.findAll(pageable);
        return result.map(engagementChecklistTemplate -> engagementChecklistTemplateMapper.engagementChecklistTemplateToEngagementChecklistTemplateDTO(engagementChecklistTemplate));
    }

    /**
     *  Get one engagementChecklistTemplate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EngagementChecklistTemplateDTO findOne(Long id) {
        log.debug("Request to get EngagementChecklistTemplate : {}", id);
        EngagementChecklistTemplate engagementChecklistTemplate = engagementChecklistTemplateRepository.findOne(id);
        EngagementChecklistTemplateDTO engagementChecklistTemplateDTO = engagementChecklistTemplateMapper.engagementChecklistTemplateToEngagementChecklistTemplateDTO(engagementChecklistTemplate);
        return engagementChecklistTemplateDTO;
    }

    /**
     *  Delete the  engagementChecklistTemplate by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EngagementChecklistTemplate : {}", id);
        engagementChecklistTemplateRepository.delete(id);
        engagementChecklistTemplateSearchRepository.delete(id);
    }

    /**
     * Search for the engagementChecklistTemplate corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EngagementChecklistTemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EngagementChecklistTemplates for query {}", query);
        Page<EngagementChecklistTemplate> result = engagementChecklistTemplateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(engagementChecklistTemplate -> engagementChecklistTemplateMapper.engagementChecklistTemplateToEngagementChecklistTemplateDTO(engagementChecklistTemplate));
    }
}
