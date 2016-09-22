package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.FeatureDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Feature.
 */
public interface FeatureService {

    /**
     * Save a feature.
     *
     * @param featureDTO the entity to save
     * @return the persisted entity
     */
    FeatureDTO save(FeatureDTO featureDTO);

    /**
     *  Get all the features.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FeatureDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" feature.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FeatureDTO findOne(Long id);

    /**
     *  Delete the "id" feature.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the feature corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FeatureDTO> search(String query, Pageable pageable);
}
