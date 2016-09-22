package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.ChecklistAnswerService;
import com.pwc.assurance.adc.domain.ChecklistAnswer;
import com.pwc.assurance.adc.repository.ChecklistAnswerRepository;
import com.pwc.assurance.adc.repository.search.ChecklistAnswerSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistAnswerDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistAnswerMapper;
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
 * Service Implementation for managing ChecklistAnswer.
 */
@Service
@Transactional
public class ChecklistAnswerServiceImpl implements ChecklistAnswerService{

    private final Logger log = LoggerFactory.getLogger(ChecklistAnswerServiceImpl.class);
    
    @Inject
    private ChecklistAnswerRepository checklistAnswerRepository;

    @Inject
    private ChecklistAnswerMapper checklistAnswerMapper;

    @Inject
    private ChecklistAnswerSearchRepository checklistAnswerSearchRepository;

    /**
     * Save a checklistAnswer.
     *
     * @param checklistAnswerDTO the entity to save
     * @return the persisted entity
     */
    public ChecklistAnswerDTO save(ChecklistAnswerDTO checklistAnswerDTO) {
        log.debug("Request to save ChecklistAnswer : {}", checklistAnswerDTO);
        ChecklistAnswer checklistAnswer = checklistAnswerMapper.checklistAnswerDTOToChecklistAnswer(checklistAnswerDTO);
        checklistAnswer = checklistAnswerRepository.save(checklistAnswer);
        ChecklistAnswerDTO result = checklistAnswerMapper.checklistAnswerToChecklistAnswerDTO(checklistAnswer);
        checklistAnswerSearchRepository.save(checklistAnswer);
        return result;
    }

    /**
     *  Get all the checklistAnswers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ChecklistAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChecklistAnswers");
        Page<ChecklistAnswer> result = checklistAnswerRepository.findAll(pageable);
        return result.map(checklistAnswer -> checklistAnswerMapper.checklistAnswerToChecklistAnswerDTO(checklistAnswer));
    }

    /**
     *  Get one checklistAnswer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ChecklistAnswerDTO findOne(Long id) {
        log.debug("Request to get ChecklistAnswer : {}", id);
        ChecklistAnswer checklistAnswer = checklistAnswerRepository.findOne(id);
        ChecklistAnswerDTO checklistAnswerDTO = checklistAnswerMapper.checklistAnswerToChecklistAnswerDTO(checklistAnswer);
        return checklistAnswerDTO;
    }

    /**
     *  Delete the  checklistAnswer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ChecklistAnswer : {}", id);
        checklistAnswerRepository.delete(id);
        checklistAnswerSearchRepository.delete(id);
    }

    /**
     * Search for the checklistAnswer corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChecklistAnswerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChecklistAnswers for query {}", query);
        Page<ChecklistAnswer> result = checklistAnswerSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(checklistAnswer -> checklistAnswerMapper.checklistAnswerToChecklistAnswerDTO(checklistAnswer));
    }
}
