package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.ChecklistHistoryChangesService;
import com.pwc.assurance.adc.domain.ChecklistHistoryChanges;
import com.pwc.assurance.adc.repository.ChecklistHistoryChangesRepository;
import com.pwc.assurance.adc.repository.search.ChecklistHistoryChangesSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistHistoryChangesDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistHistoryChangesMapper;
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
 * Service Implementation for managing ChecklistHistoryChanges.
 */
@Service
@Transactional
public class ChecklistHistoryChangesServiceImpl implements ChecklistHistoryChangesService{

    private final Logger log = LoggerFactory.getLogger(ChecklistHistoryChangesServiceImpl.class);
    
    @Inject
    private ChecklistHistoryChangesRepository checklistHistoryChangesRepository;

    @Inject
    private ChecklistHistoryChangesMapper checklistHistoryChangesMapper;

    @Inject
    private ChecklistHistoryChangesSearchRepository checklistHistoryChangesSearchRepository;

    /**
     * Save a checklistHistoryChanges.
     *
     * @param checklistHistoryChangesDTO the entity to save
     * @return the persisted entity
     */
    public ChecklistHistoryChangesDTO save(ChecklistHistoryChangesDTO checklistHistoryChangesDTO) {
        log.debug("Request to save ChecklistHistoryChanges : {}", checklistHistoryChangesDTO);
        ChecklistHistoryChanges checklistHistoryChanges = checklistHistoryChangesMapper.checklistHistoryChangesDTOToChecklistHistoryChanges(checklistHistoryChangesDTO);
        checklistHistoryChanges = checklistHistoryChangesRepository.save(checklistHistoryChanges);
        ChecklistHistoryChangesDTO result = checklistHistoryChangesMapper.checklistHistoryChangesToChecklistHistoryChangesDTO(checklistHistoryChanges);
        checklistHistoryChangesSearchRepository.save(checklistHistoryChanges);
        return result;
    }

    /**
     *  Get all the checklistHistoryChanges.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChecklistHistoryChangesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChecklistHistoryChanges");
        Page<ChecklistHistoryChanges> result = checklistHistoryChangesRepository.findAll(pageable);
        return result.map(checklistHistoryChanges -> checklistHistoryChangesMapper.checklistHistoryChangesToChecklistHistoryChangesDTO(checklistHistoryChanges));
    }

    /**
     *  Get one checklistHistoryChanges by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ChecklistHistoryChangesDTO findOne(Long id) {
        log.debug("Request to get ChecklistHistoryChanges : {}", id);
        ChecklistHistoryChanges checklistHistoryChanges = checklistHistoryChangesRepository.findOne(id);
        ChecklistHistoryChangesDTO checklistHistoryChangesDTO = checklistHistoryChangesMapper.checklistHistoryChangesToChecklistHistoryChangesDTO(checklistHistoryChanges);
        return checklistHistoryChangesDTO;
    }

    /**
     *  Delete the  checklistHistoryChanges by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ChecklistHistoryChanges : {}", id);
        checklistHistoryChangesRepository.delete(id);
        checklistHistoryChangesSearchRepository.delete(id);
    }

    /**
     * Search for the checklistHistoryChanges corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChecklistHistoryChangesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChecklistHistoryChanges for query {}", query);
        Page<ChecklistHistoryChanges> result = checklistHistoryChangesSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(checklistHistoryChanges -> checklistHistoryChangesMapper.checklistHistoryChangesToChecklistHistoryChangesDTO(checklistHistoryChanges));
    }
}
