package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ChecklistAnswerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ChecklistAnswer.
 */
public interface ChecklistAnswerService {

    /**
     * Save a checklistAnswer.
     *
     * @param checklistAnswerDTO the entity to save
     * @return the persisted entity
     */
    ChecklistAnswerDTO save(ChecklistAnswerDTO checklistAnswerDTO);

    /**
     *  Get all the checklistAnswers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistAnswerDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" checklistAnswer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChecklistAnswerDTO findOne(Long id);

    /**
     *  Delete the "id" checklistAnswer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the checklistAnswer corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistAnswerDTO> search(String query, Pageable pageable);
}
