package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.ChecklistService;
import com.pwc.assurance.adc.domain.Checklist;
import com.pwc.assurance.adc.repository.ChecklistRepository;
import com.pwc.assurance.adc.repository.search.ChecklistSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistMapper;
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
 * Service Implementation for managing Checklist.
 */
@Service
@Transactional
public class ChecklistServiceImpl implements ChecklistService{

    private final Logger log = LoggerFactory.getLogger(ChecklistServiceImpl.class);
    
    @Inject
    private ChecklistRepository checklistRepository;

    @Inject
    private ChecklistMapper checklistMapper;

    @Inject
    private ChecklistSearchRepository checklistSearchRepository;

    /**
     * Save a checklist.
     *
     * @param checklistDTO the entity to save
     * @return the persisted entity
     */
    public ChecklistDTO save(ChecklistDTO checklistDTO) {
        log.debug("Request to save Checklist : {}", checklistDTO);
        Checklist checklist = checklistMapper.checklistDTOToChecklist(checklistDTO);
        checklist = checklistRepository.save(checklist);
        ChecklistDTO result = checklistMapper.checklistToChecklistDTO(checklist);
        checklistSearchRepository.save(checklist);
        return result;
    }

    /**
     *  Get all the checklists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChecklistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Checklists");
        Page<Checklist> result = checklistRepository.findAll(pageable);
        return result.map(checklist -> checklistMapper.checklistToChecklistDTO(checklist));
    }

    /**
     *  Get one checklist by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ChecklistDTO findOne(Long id) {
        log.debug("Request to get Checklist : {}", id);
        Checklist checklist = checklistRepository.findOneWithEagerRelationships(id);
        ChecklistDTO checklistDTO = checklistMapper.checklistToChecklistDTO(checklist);
        return checklistDTO;
    }

    /**
     *  Delete the  checklist by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Checklist : {}", id);
        checklistRepository.delete(id);
        checklistSearchRepository.delete(id);
    }

    /**
     * Search for the checklist corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChecklistDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Checklists for query {}", query);
        Page<Checklist> result = checklistSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(checklist -> checklistMapper.checklistToChecklistDTO(checklist));
    }
}
