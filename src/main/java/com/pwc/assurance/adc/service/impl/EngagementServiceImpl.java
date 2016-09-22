package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.EngagementService;
import com.pwc.assurance.adc.domain.Engagement;
import com.pwc.assurance.adc.repository.EngagementRepository;
import com.pwc.assurance.adc.repository.search.EngagementSearchRepository;
import com.pwc.assurance.adc.service.dto.EngagementDTO;
import com.pwc.assurance.adc.service.mapper.EngagementMapper;
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
 * Service Implementation for managing Engagement.
 */
@Service
@Transactional
public class EngagementServiceImpl implements EngagementService{

    private final Logger log = LoggerFactory.getLogger(EngagementServiceImpl.class);
    
    @Inject
    private EngagementRepository engagementRepository;

    @Inject
    private EngagementMapper engagementMapper;

    @Inject
    private EngagementSearchRepository engagementSearchRepository;

    /**
     * Save a engagement.
     *
     * @param engagementDTO the entity to save
     * @return the persisted entity
     */
    public EngagementDTO save(EngagementDTO engagementDTO) {
        log.debug("Request to save Engagement : {}", engagementDTO);
        Engagement engagement = engagementMapper.engagementDTOToEngagement(engagementDTO);
        engagement = engagementRepository.save(engagement);
        EngagementDTO result = engagementMapper.engagementToEngagementDTO(engagement);
        engagementSearchRepository.save(engagement);
        return result;
    }

    /**
     *  Get all the engagements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<EngagementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Engagements");
        Page<Engagement> result = engagementRepository.findAll(pageable);
        return result.map(engagement -> engagementMapper.engagementToEngagementDTO(engagement));
    }

    /**
     *  Get one engagement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EngagementDTO findOne(Long id) {
        log.debug("Request to get Engagement : {}", id);
        Engagement engagement = engagementRepository.findOne(id);
        EngagementDTO engagementDTO = engagementMapper.engagementToEngagementDTO(engagement);
        return engagementDTO;
    }

    /**
     *  Delete the  engagement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Engagement : {}", id);
        engagementRepository.delete(id);
        engagementSearchRepository.delete(id);
    }

    /**
     * Search for the engagement corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EngagementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Engagements for query {}", query);
        Page<Engagement> result = engagementSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(engagement -> engagementMapper.engagementToEngagementDTO(engagement));
    }
}
