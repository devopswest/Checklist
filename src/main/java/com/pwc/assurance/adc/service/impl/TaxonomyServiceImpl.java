package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.TaxonomyService;
import com.pwc.assurance.adc.domain.Taxonomy;
import com.pwc.assurance.adc.repository.TaxonomyRepository;
import com.pwc.assurance.adc.repository.search.TaxonomySearchRepository;
import com.pwc.assurance.adc.service.dto.TaxonomyDTO;
import com.pwc.assurance.adc.service.mapper.TaxonomyMapper;
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
 * Service Implementation for managing Taxonomy.
 */
@Service
@Transactional
public class TaxonomyServiceImpl implements TaxonomyService{

    private final Logger log = LoggerFactory.getLogger(TaxonomyServiceImpl.class);
    
    @Inject
    private TaxonomyRepository taxonomyRepository;

    @Inject
    private TaxonomyMapper taxonomyMapper;

    @Inject
    private TaxonomySearchRepository taxonomySearchRepository;

    /**
     * Save a taxonomy.
     *
     * @param taxonomyDTO the entity to save
     * @return the persisted entity
     */
    public TaxonomyDTO save(TaxonomyDTO taxonomyDTO) {
        log.debug("Request to save Taxonomy : {}", taxonomyDTO);
        Taxonomy taxonomy = taxonomyMapper.taxonomyDTOToTaxonomy(taxonomyDTO);
        taxonomy = taxonomyRepository.save(taxonomy);
        TaxonomyDTO result = taxonomyMapper.taxonomyToTaxonomyDTO(taxonomy);
        taxonomySearchRepository.save(taxonomy);
        return result;
    }

    /**
     *  Get all the taxonomies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TaxonomyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Taxonomies");
        Page<Taxonomy> result = taxonomyRepository.findAll(pageable);
        return result.map(taxonomy -> taxonomyMapper.taxonomyToTaxonomyDTO(taxonomy));
    }

    /**
     *  Get one taxonomy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TaxonomyDTO findOne(Long id) {
        log.debug("Request to get Taxonomy : {}", id);
        Taxonomy taxonomy = taxonomyRepository.findOne(id);
        TaxonomyDTO taxonomyDTO = taxonomyMapper.taxonomyToTaxonomyDTO(taxonomy);
        return taxonomyDTO;
    }

    /**
     *  Delete the  taxonomy by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Taxonomy : {}", id);
        taxonomyRepository.delete(id);
        taxonomySearchRepository.delete(id);
    }

    /**
     * Search for the taxonomy corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaxonomyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Taxonomies for query {}", query);
        Page<Taxonomy> result = taxonomySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(taxonomy -> taxonomyMapper.taxonomyToTaxonomyDTO(taxonomy));
    }
}
