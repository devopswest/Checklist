package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.FeatureAuthorityService;
import com.pwc.assurance.adc.domain.FeatureAuthority;
import com.pwc.assurance.adc.repository.FeatureAuthorityRepository;
import com.pwc.assurance.adc.repository.search.FeatureAuthoritySearchRepository;
import com.pwc.assurance.adc.service.dto.FeatureAuthorityDTO;
import com.pwc.assurance.adc.service.mapper.FeatureAuthorityMapper;
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
 * Service Implementation for managing FeatureAuthority.
 */
@Service
@Transactional
public class FeatureAuthorityServiceImpl implements FeatureAuthorityService{

    private final Logger log = LoggerFactory.getLogger(FeatureAuthorityServiceImpl.class);
    
    @Inject
    private FeatureAuthorityRepository featureAuthorityRepository;

    @Inject
    private FeatureAuthorityMapper featureAuthorityMapper;

    @Inject
    private FeatureAuthoritySearchRepository featureAuthoritySearchRepository;

    /**
     * Save a featureAuthority.
     *
     * @param featureAuthorityDTO the entity to save
     * @return the persisted entity
     */
    public FeatureAuthorityDTO save(FeatureAuthorityDTO featureAuthorityDTO) {
        log.debug("Request to save FeatureAuthority : {}", featureAuthorityDTO);
        FeatureAuthority featureAuthority = featureAuthorityMapper.featureAuthorityDTOToFeatureAuthority(featureAuthorityDTO);
        featureAuthority = featureAuthorityRepository.save(featureAuthority);
        FeatureAuthorityDTO result = featureAuthorityMapper.featureAuthorityToFeatureAuthorityDTO(featureAuthority);
        featureAuthoritySearchRepository.save(featureAuthority);
        return result;
    }

    /**
     *  Get all the featureAuthorities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<FeatureAuthorityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FeatureAuthorities");
        Page<FeatureAuthority> result = featureAuthorityRepository.findAll(pageable);
        return result.map(featureAuthority -> featureAuthorityMapper.featureAuthorityToFeatureAuthorityDTO(featureAuthority));
    }

    /**
     *  Get one featureAuthority by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FeatureAuthorityDTO findOne(Long id) {
        log.debug("Request to get FeatureAuthority : {}", id);
        FeatureAuthority featureAuthority = featureAuthorityRepository.findOne(id);
        FeatureAuthorityDTO featureAuthorityDTO = featureAuthorityMapper.featureAuthorityToFeatureAuthorityDTO(featureAuthority);
        return featureAuthorityDTO;
    }

    /**
     *  Delete the  featureAuthority by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FeatureAuthority : {}", id);
        featureAuthorityRepository.delete(id);
        featureAuthoritySearchRepository.delete(id);
    }

    /**
     * Search for the featureAuthority corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FeatureAuthorityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FeatureAuthorities for query {}", query);
        Page<FeatureAuthority> result = featureAuthoritySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(featureAuthority -> featureAuthorityMapper.featureAuthorityToFeatureAuthorityDTO(featureAuthority));
    }
}
