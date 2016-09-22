package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ClientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Client.
 */
public interface ClientService {

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save
     * @return the persisted entity
     */
    ClientDTO save(ClientDTO clientDTO);

    /**
     *  Get all the clients.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ClientDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" client.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClientDTO findOne(Long id);

    /**
     *  Delete the "id" client.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the client corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ClientDTO> search(String query, Pageable pageable);
}
