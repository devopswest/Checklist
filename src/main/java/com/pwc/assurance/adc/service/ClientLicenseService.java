package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.ClientLicenseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ClientLicense.
 */
public interface ClientLicenseService {

    /**
     * Save a clientLicense.
     *
     * @param clientLicenseDTO the entity to save
     * @return the persisted entity
     */
    ClientLicenseDTO save(ClientLicenseDTO clientLicenseDTO);

    /**
     *  Get all the clientLicenses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ClientLicenseDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" clientLicense.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClientLicenseDTO findOne(Long id);

    /**
     *  Delete the "id" clientLicense.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the clientLicense corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ClientLicenseDTO> search(String query, Pageable pageable);
}
