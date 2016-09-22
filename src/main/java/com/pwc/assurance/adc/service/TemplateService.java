package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.TemplateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Template.
 */
public interface TemplateService {

    /**
     * Save a template.
     *
     * @param templateDTO the entity to save
     * @return the persisted entity
     */
    TemplateDTO save(TemplateDTO templateDTO);

    /**
     *  Get all the templates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TemplateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" template.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TemplateDTO findOne(Long id);

    /**
     *  Delete the "id" template.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the template corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TemplateDTO> search(String query, Pageable pageable);
}
