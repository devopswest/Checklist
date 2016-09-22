package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.DisclosureRequirementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing DisclosureRequirement.
 */
public interface DisclosureRequirementService {

    /**
     * Save a disclosureRequirement.
     *
     * @param disclosureRequirementDTO the entity to save
     * @return the persisted entity
     */
    DisclosureRequirementDTO save(DisclosureRequirementDTO disclosureRequirementDTO);

    /**
     *  Get all the disclosureRequirements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DisclosureRequirementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" disclosureRequirement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DisclosureRequirementDTO findOne(Long id);

    /**
     *  Delete the "id" disclosureRequirement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the disclosureRequirement corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DisclosureRequirementDTO> search(String query, Pageable pageable);
}
