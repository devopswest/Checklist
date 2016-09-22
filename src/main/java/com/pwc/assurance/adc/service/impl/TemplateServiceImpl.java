package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.TemplateService;
import com.pwc.assurance.adc.domain.Template;
import com.pwc.assurance.adc.repository.TemplateRepository;
import com.pwc.assurance.adc.repository.search.TemplateSearchRepository;
import com.pwc.assurance.adc.service.dto.TemplateDTO;
import com.pwc.assurance.adc.service.mapper.TemplateMapper;
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
 * Service Implementation for managing Template.
 */
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService{

    private final Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);
    
    @Inject
    private TemplateRepository templateRepository;

    @Inject
    private TemplateMapper templateMapper;

    @Inject
    private TemplateSearchRepository templateSearchRepository;

    /**
     * Save a template.
     *
     * @param templateDTO the entity to save
     * @return the persisted entity
     */
    public TemplateDTO save(TemplateDTO templateDTO) {
        log.debug("Request to save Template : {}", templateDTO);
        Template template = templateMapper.templateDTOToTemplate(templateDTO);
        template = templateRepository.save(template);
        TemplateDTO result = templateMapper.templateToTemplateDTO(template);
        templateSearchRepository.save(template);
        return result;
    }

    /**
     *  Get all the templates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Templates");
        Page<Template> result = templateRepository.findAll(pageable);
        return result.map(template -> templateMapper.templateToTemplateDTO(template));
    }

    /**
     *  Get one template by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TemplateDTO findOne(Long id) {
        log.debug("Request to get Template : {}", id);
        Template template = templateRepository.findOne(id);
        TemplateDTO templateDTO = templateMapper.templateToTemplateDTO(template);
        return templateDTO;
    }

    /**
     *  Delete the  template by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Template : {}", id);
        templateRepository.delete(id);
        templateSearchRepository.delete(id);
    }

    /**
     * Search for the template corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Templates for query {}", query);
        Page<Template> result = templateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(template -> templateMapper.templateToTemplateDTO(template));
    }
}
