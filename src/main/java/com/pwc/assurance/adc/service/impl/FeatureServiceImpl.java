package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.FeatureService;
import com.pwc.assurance.adc.domain.Feature;
import com.pwc.assurance.adc.repository.FeatureRepository;
import com.pwc.assurance.adc.repository.search.FeatureSearchRepository;
import com.pwc.assurance.adc.service.dto.FeatureDTO;
import com.pwc.assurance.adc.service.mapper.FeatureMapper;
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
 * Service Implementation for managing Feature.
 */
@Service
@Transactional
public class FeatureServiceImpl implements FeatureService{

    private final Logger log = LoggerFactory.getLogger(FeatureServiceImpl.class);
    
    @Inject
    private FeatureRepository featureRepository;

    @Inject
    private FeatureMapper featureMapper;

    @Inject
    private FeatureSearchRepository featureSearchRepository;

    /**
     * Save a feature.
     *
     * @param featureDTO the entity to save
     * @return the persisted entity
     */
    public FeatureDTO save(FeatureDTO featureDTO) {
        log.debug("Request to save Feature : {}", featureDTO);
        Feature feature = featureMapper.featureDTOToFeature(featureDTO);
        feature = featureRepository.save(feature);
        FeatureDTO result = featureMapper.featureToFeatureDTO(feature);
        featureSearchRepository.save(feature);
        return result;
    }

    /**
     *  Get all the features.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<FeatureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Features");
        Page<Feature> result = featureRepository.findAll(pageable);
        return result.map(feature -> featureMapper.featureToFeatureDTO(feature));
    }

    /**
     *  Get one feature by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FeatureDTO findOne(Long id) {
        log.debug("Request to get Feature : {}", id);
        Feature feature = featureRepository.findOne(id);
        FeatureDTO featureDTO = featureMapper.featureToFeatureDTO(feature);
        return featureDTO;
    }

    /**
     *  Delete the  feature by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Feature : {}", id);
        featureRepository.delete(id);
        featureSearchRepository.delete(id);
    }

    /**
     * Search for the feature corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FeatureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Features for query {}", query);
        Page<Feature> result = featureSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(feature -> featureMapper.featureToFeatureDTO(feature));
    }
}
