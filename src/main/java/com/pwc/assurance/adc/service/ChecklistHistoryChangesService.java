package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ChecklistHistoryChangesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ChecklistHistoryChanges.
 */
public interface ChecklistHistoryChangesService {

    /**
     * Save a checklistHistoryChanges.
     *
     * @param checklistHistoryChangesDTO the entity to save
     * @return the persisted entity
     */
    ChecklistHistoryChangesDTO save(ChecklistHistoryChangesDTO checklistHistoryChangesDTO);

    /**
     *  Get all the checklistHistoryChanges.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistHistoryChangesDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" checklistHistoryChanges.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChecklistHistoryChangesDTO findOne(Long id);

    /**
     *  Delete the "id" checklistHistoryChanges.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the checklistHistoryChanges corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChecklistHistoryChangesDTO> search(String query, Pageable pageable);
}
