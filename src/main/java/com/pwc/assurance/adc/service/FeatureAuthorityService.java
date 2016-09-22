package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.FeatureAuthorityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing FeatureAuthority.
 */
public interface FeatureAuthorityService {

    /**
     * Save a featureAuthority.
     *
     * @param featureAuthorityDTO the entity to save
     * @return the persisted entity
     */
    FeatureAuthorityDTO save(FeatureAuthorityDTO featureAuthorityDTO);

    /**
     *  Get all the featureAuthorities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FeatureAuthorityDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" featureAuthority.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FeatureAuthorityDTO findOne(Long id);

    /**
     *  Delete the "id" featureAuthority.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the featureAuthority corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FeatureAuthorityDTO> search(String query, Pageable pageable);
}
