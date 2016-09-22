package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.DisclosureRequirementTagService;
import com.pwc.assurance.adc.domain.DisclosureRequirementTag;
import com.pwc.assurance.adc.repository.DisclosureRequirementTagRepository;
import com.pwc.assurance.adc.repository.search.DisclosureRequirementTagSearchRepository;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementTagDTO;
import com.pwc.assurance.adc.service.mapper.DisclosureRequirementTagMapper;
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
 * Service Implementation for managing DisclosureRequirementTag.
 */
@Service
@Transactional
public class DisclosureRequirementTagServiceImpl implements DisclosureRequirementTagService{

    private final Logger log = LoggerFactory.getLogger(DisclosureRequirementTagServiceImpl.class);
    
    @Inject
    private DisclosureRequirementTagRepository disclosureRequirementTagRepository;

    @Inject
    private DisclosureRequirementTagMapper disclosureRequirementTagMapper;

    @Inject
    private DisclosureRequirementTagSearchRepository disclosureRequirementTagSearchRepository;

    /**
     * Save a disclosureRequirementTag.
     *
     * @param disclosureRequirementTagDTO the entity to save
     * @return the persisted entity
     */
    public DisclosureRequirementTagDTO save(DisclosureRequirementTagDTO disclosureRequirementTagDTO) {
        log.debug("Request to save DisclosureRequirementTag : {}", disclosureRequirementTagDTO);
        DisclosureRequirementTag disclosureRequirementTag = disclosureRequirementTagMapper.disclosureRequirementTagDTOToDisclosureRequirementTag(disclosureRequirementTagDTO);
        disclosureRequirementTag = disclosureRequirementTagRepository.save(disclosureRequirementTag);
        DisclosureRequirementTagDTO result = disclosureRequirementTagMapper.disclosureRequirementTagToDisclosureRequirementTagDTO(disclosureRequirementTag);
        disclosureRequirementTagSearchRepository.save(disclosureRequirementTag);
        return result;
    }

    /**
     *  Get all the disclosureRequirementTags.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DisclosureRequirementTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DisclosureRequirementTags");
        Page<DisclosureRequirementTag> result = disclosureRequirementTagRepository.findAll(pageable);
        return result.map(disclosureRequirementTag -> disclosureRequirementTagMapper.disclosureRequirementTagToDisclosureRequirementTagDTO(disclosureRequirementTag));
    }

    /**
     *  Get one disclosureRequirementTag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DisclosureRequirementTagDTO findOne(Long id) {
        log.debug("Request to get DisclosureRequirementTag : {}", id);
        DisclosureRequirementTag disclosureRequirementTag = disclosureRequirementTagRepository.findOne(id);
        DisclosureRequirementTagDTO disclosureRequirementTagDTO = disclosureRequirementTagMapper.disclosureRequirementTagToDisclosureRequirementTagDTO(disclosureRequirementTag);
        return disclosureRequirementTagDTO;
    }

    /**
     *  Delete the  disclosureRequirementTag by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DisclosureRequirementTag : {}", id);
        disclosureRequirementTagRepository.delete(id);
        disclosureRequirementTagSearchRepository.delete(id);
    }

    /**
     * Search for the disclosureRequirementTag corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DisclosureRequirementTagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DisclosureRequirementTags for query {}", query);
        Page<DisclosureRequirementTag> result = disclosureRequirementTagSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(disclosureRequirementTag -> disclosureRequirementTagMapper.disclosureRequirementTagToDisclosureRequirementTagDTO(disclosureRequirementTag));
    }
}
