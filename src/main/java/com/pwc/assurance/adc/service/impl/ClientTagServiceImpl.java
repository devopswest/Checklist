package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.ClientTagService;
import com.pwc.assurance.adc.domain.ClientTag;
import com.pwc.assurance.adc.repository.ClientTagRepository;
import com.pwc.assurance.adc.repository.search.ClientTagSearchRepository;
import com.pwc.assurance.adc.service.dto.ClientTagDTO;
import com.pwc.assurance.adc.service.mapper.ClientTagMapper;
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
 * Service Implementation for managing ClientTag.
 */
@Service
@Transactional
public class ClientTagServiceImpl implements ClientTagService{

    private final Logger log = LoggerFactory.getLogger(ClientTagServiceImpl.class);
    
    @Inject
    private ClientTagRepository clientTagRepository;

    @Inject
    private ClientTagMapper clientTagMapper;

    @Inject
    private ClientTagSearchRepository clientTagSearchRepository;

    /**
     * Save a clientTag.
     *
     * @param clientTagDTO the entity to save
     * @return the persisted entity
     */
    public ClientTagDTO save(ClientTagDTO clientTagDTO) {
        log.debug("Request to save ClientTag : {}", clientTagDTO);
        ClientTag clientTag = clientTagMapper.clientTagDTOToClientTag(clientTagDTO);
        clientTag = clientTagRepository.save(clientTag);
        ClientTagDTO result = clientTagMapper.clientTagToClientTagDTO(clientTag);
        clientTagSearchRepository.save(clientTag);
        return result;
    }

    /**
     *  Get all the clientTags.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ClientTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClientTags");
        Page<ClientTag> result = clientTagRepository.findAll(pageable);
        return result.map(clientTag -> clientTagMapper.clientTagToClientTagDTO(clientTag));
    }

    /**
     *  Get one clientTag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ClientTagDTO findOne(Long id) {
        log.debug("Request to get ClientTag : {}", id);
        ClientTag clientTag = clientTagRepository.findOne(id);
        ClientTagDTO clientTagDTO = clientTagMapper.clientTagToClientTagDTO(clientTag);
        return clientTagDTO;
    }

    /**
     *  Delete the  clientTag by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ClientTag : {}", id);
        clientTagRepository.delete(id);
        clientTagSearchRepository.delete(id);
    }

    /**
     * Search for the clientTag corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClientTagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ClientTags for query {}", query);
        Page<ClientTag> result = clientTagSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(clientTag -> clientTagMapper.clientTagToClientTagDTO(clientTag));
    }
}
