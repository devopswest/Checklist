package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.ClientLicenseService;
import com.pwc.assurance.adc.domain.ClientLicense;
import com.pwc.assurance.adc.repository.ClientLicenseRepository;
import com.pwc.assurance.adc.repository.search.ClientLicenseSearchRepository;
import com.pwc.assurance.adc.service.dto.ClientLicenseDTO;
import com.pwc.assurance.adc.service.mapper.ClientLicenseMapper;
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
 * Service Implementation for managing ClientLicense.
 */
@Service
@Transactional
public class ClientLicenseServiceImpl implements ClientLicenseService{

    private final Logger log = LoggerFactory.getLogger(ClientLicenseServiceImpl.class);
    
    @Inject
    private ClientLicenseRepository clientLicenseRepository;

    @Inject
    private ClientLicenseMapper clientLicenseMapper;

    @Inject
    private ClientLicenseSearchRepository clientLicenseSearchRepository;

    /**
     * Save a clientLicense.
     *
     * @param clientLicenseDTO the entity to save
     * @return the persisted entity
     */
    public ClientLicenseDTO save(ClientLicenseDTO clientLicenseDTO) {
        log.debug("Request to save ClientLicense : {}", clientLicenseDTO);
        ClientLicense clientLicense = clientLicenseMapper.clientLicenseDTOToClientLicense(clientLicenseDTO);
        clientLicense = clientLicenseRepository.save(clientLicense);
        ClientLicenseDTO result = clientLicenseMapper.clientLicenseToClientLicenseDTO(clientLicense);
        clientLicenseSearchRepository.save(clientLicense);
        return result;
    }

    /**
     *  Get all the clientLicenses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ClientLicenseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClientLicenses");
        Page<ClientLicense> result = clientLicenseRepository.findAll(pageable);
        return result.map(clientLicense -> clientLicenseMapper.clientLicenseToClientLicenseDTO(clientLicense));
    }

    /**
     *  Get one clientLicense by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ClientLicenseDTO findOne(Long id) {
        log.debug("Request to get ClientLicense : {}", id);
        ClientLicense clientLicense = clientLicenseRepository.findOne(id);
        ClientLicenseDTO clientLicenseDTO = clientLicenseMapper.clientLicenseToClientLicenseDTO(clientLicense);
        return clientLicenseDTO;
    }

    /**
     *  Delete the  clientLicense by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ClientLicense : {}", id);
        clientLicenseRepository.delete(id);
        clientLicenseSearchRepository.delete(id);
    }

    /**
     * Search for the clientLicense corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClientLicenseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ClientLicenses for query {}", query);
        Page<ClientLicense> result = clientLicenseSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(clientLicense -> clientLicenseMapper.clientLicenseToClientLicenseDTO(clientLicense));
    }
}
