package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.RequirementService;
import com.pwc.assurance.adc.domain.Requirement;
import com.pwc.assurance.adc.repository.RequirementRepository;
import com.pwc.assurance.adc.repository.search.RequirementSearchRepository;
import com.pwc.assurance.adc.service.dto.RequirementDTO;
import com.pwc.assurance.adc.service.mapper.RequirementMapper;
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
 * Service Implementation for managing Requirement.
 */
@Service
@Transactional
public class RequirementServiceImpl implements RequirementService{

    private final Logger log = LoggerFactory.getLogger(RequirementServiceImpl.class);
    
    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private RequirementMapper requirementMapper;

    @Inject
    private RequirementSearchRepository requirementSearchRepository;

    /**
     * Save a requirement.
     *
     * @param requirementDTO the entity to save
     * @return the persisted entity
     */
    public RequirementDTO save(RequirementDTO requirementDTO) {
        log.debug("Request to save Requirement : {}", requirementDTO);
        Requirement requirement = requirementMapper.requirementDTOToRequirement(requirementDTO);
        requirement = requirementRepository.save(requirement);
        RequirementDTO result = requirementMapper.requirementToRequirementDTO(requirement);
        requirementSearchRepository.save(requirement);
        return result;
    }

    /**
     *  Get all the requirements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<RequirementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Requirements");
        Page<Requirement> result = requirementRepository.findAll(pageable);
        return result.map(requirement -> requirementMapper.requirementToRequirementDTO(requirement));
    }

    /**
     *  Get one requirement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RequirementDTO findOne(Long id) {
        log.debug("Request to get Requirement : {}", id);
        Requirement requirement = requirementRepository.findOneWithEagerRelationships(id);
        RequirementDTO requirementDTO = requirementMapper.requirementToRequirementDTO(requirement);
        return requirementDTO;
    }

    /**
     *  Delete the  requirement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Requirement : {}", id);
        requirementRepository.delete(id);
        requirementSearchRepository.delete(id);
    }

    /**
     * Search for the requirement corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RequirementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Requirements for query {}", query);
        Page<Requirement> result = requirementSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(requirement -> requirementMapper.requirementToRequirementDTO(requirement));
    }
}
