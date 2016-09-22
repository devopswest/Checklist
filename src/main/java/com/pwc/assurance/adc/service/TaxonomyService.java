package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.TaxonomyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Taxonomy.
 */
public interface TaxonomyService {

    /**
     * Save a taxonomy.
     *
     * @param taxonomyDTO the entity to save
     * @return the persisted entity
     */
    TaxonomyDTO save(TaxonomyDTO taxonomyDTO);

    /**
     *  Get all the taxonomies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaxonomyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" taxonomy.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaxonomyDTO findOne(Long id);

    /**
     *  Delete the "id" taxonomy.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the taxonomy corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaxonomyDTO> search(String query, Pageable pageable);
}
