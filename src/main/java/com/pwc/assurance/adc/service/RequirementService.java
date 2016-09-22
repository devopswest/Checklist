package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.RequirementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Requirement.
 */
public interface RequirementService {

    /**
     * Save a requirement.
     *
     * @param requirementDTO the entity to save
     * @return the persisted entity
     */
    RequirementDTO save(RequirementDTO requirementDTO);

    /**
     *  Get all the requirements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RequirementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" requirement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RequirementDTO findOne(Long id);

    /**
     *  Delete the "id" requirement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the requirement corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RequirementDTO> search(String query, Pageable pageable);
}
