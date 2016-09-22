package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ClientTagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ClientTag.
 */
public interface ClientTagService {

    /**
     * Save a clientTag.
     *
     * @param clientTagDTO the entity to save
     * @return the persisted entity
     */
    ClientTagDTO save(ClientTagDTO clientTagDTO);

    /**
     *  Get all the clientTags.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ClientTagDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" clientTag.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClientTagDTO findOne(Long id);

    /**
     *  Delete the "id" clientTag.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the clientTag corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ClientTagDTO> search(String query, Pageable pageable);
}
