package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.DisclosureRequirementTagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing DisclosureRequirementTag.
 */
public interface DisclosureRequirementTagService {

    /**
     * Save a disclosureRequirementTag.
     *
     * @param disclosureRequirementTagDTO the entity to save
     * @return the persisted entity
     */
    DisclosureRequirementTagDTO save(DisclosureRequirementTagDTO disclosureRequirementTagDTO);

    /**
     *  Get all the disclosureRequirementTags.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DisclosureRequirementTagDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" disclosureRequirementTag.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DisclosureRequirementTagDTO findOne(Long id);

    /**
     *  Delete the "id" disclosureRequirementTag.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the disclosureRequirementTag corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DisclosureRequirementTagDTO> search(String query, Pageable pageable);
}
