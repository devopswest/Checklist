package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.DisclosureRequirementService;
import com.pwc.assurance.adc.domain.DisclosureRequirement;
import com.pwc.assurance.adc.repository.DisclosureRequirementRepository;
import com.pwc.assurance.adc.repository.search.DisclosureRequirementSearchRepository;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementDTO;
import com.pwc.assurance.adc.service.mapper.DisclosureRequirementMapper;
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
 * Service Implementation for managing DisclosureRequirement.
 */
@Service
@Transactional
public class DisclosureRequirementServiceImpl implements DisclosureRequirementService{

    private final Logger log = LoggerFactory.getLogger(DisclosureRequirementServiceImpl.class);
    
    @Inject
    private DisclosureRequirementRepository disclosureRequirementRepository;

    @Inject
    private DisclosureRequirementMapper disclosureRequirementMapper;

    @Inject
    private DisclosureRequirementSearchRepository disclosureRequirementSearchRepository;

    /**
     * Save a disclosureRequirement.
     *
     * @param disclosureRequirementDTO the entity to save
     * @return the persisted entity
     */
    public DisclosureRequirementDTO save(DisclosureRequirementDTO disclosureRequirementDTO) {
        log.debug("Request to save DisclosureRequirement : {}", disclosureRequirementDTO);
        DisclosureRequirement disclosureRequirement = disclosureRequirementMapper.disclosureRequirementDTOToDisclosureRequirement(disclosureRequirementDTO);
        disclosureRequirement = disclosureRequirementRepository.save(disclosureRequirement);
        DisclosureRequirementDTO result = disclosureRequirementMapper.disclosureRequirementToDisclosureRequirementDTO(disclosureRequirement);
        disclosureRequirementSearchRepository.save(disclosureRequirement);
        return result;
    }

    /**
     *  Get all the disclosureRequirements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DisclosureRequirementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DisclosureRequirements");
        Page<DisclosureRequirement> result = disclosureRequirementRepository.findAll(pageable);
        return result.map(disclosureRequirement -> disclosureRequirementMapper.disclosureRequirementToDisclosureRequirementDTO(disclosureRequirement));
    }

    /**
     *  Get one disclosureRequirement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DisclosureRequirementDTO findOne(Long id) {
        log.debug("Request to get DisclosureRequirement : {}", id);
        DisclosureRequirement disclosureRequirement = disclosureRequirementRepository.findOne(id);
        DisclosureRequirementDTO disclosureRequirementDTO = disclosureRequirementMapper.disclosureRequirementToDisclosureRequirementDTO(disclosureRequirement);
        return disclosureRequirementDTO;
    }

    /**
     *  Delete the  disclosureRequirement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DisclosureRequirement : {}", id);
        disclosureRequirementRepository.delete(id);
        disclosureRequirementSearchRepository.delete(id);
    }

    /**
     * Search for the disclosureRequirement corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DisclosureRequirementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DisclosureRequirements for query {}", query);
        Page<DisclosureRequirement> result = disclosureRequirementSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(disclosureRequirement -> disclosureRequirementMapper.disclosureRequirementToDisclosureRequirementDTO(disclosureRequirement));
    }
}
