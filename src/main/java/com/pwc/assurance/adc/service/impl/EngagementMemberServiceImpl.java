package com.pwc.assurance.adc.service.impl;

import com.pwc.assurance.adc.service.EngagementMemberService;
import com.pwc.assurance.adc.domain.EngagementMember;
import com.pwc.assurance.adc.repository.EngagementMemberRepository;
import com.pwc.assurance.adc.repository.search.EngagementMemberSearchRepository;
import com.pwc.assurance.adc.service.dto.EngagementMemberDTO;
import com.pwc.assurance.adc.service.mapper.EngagementMemberMapper;
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
 * Service Implementation for managing EngagementMember.
 */
@Service
@Transactional
public class EngagementMemberServiceImpl implements EngagementMemberService{

    private final Logger log = LoggerFactory.getLogger(EngagementMemberServiceImpl.class);
    
    @Inject
    private EngagementMemberRepository engagementMemberRepository;

    @Inject
    private EngagementMemberMapper engagementMemberMapper;

    @Inject
    private EngagementMemberSearchRepository engagementMemberSearchRepository;

    /**
     * Save a engagementMember.
     *
     * @param engagementMemberDTO the entity to save
     * @return the persisted entity
     */
    public EngagementMemberDTO save(EngagementMemberDTO engagementMemberDTO) {
        log.debug("Request to save EngagementMember : {}", engagementMemberDTO);
        EngagementMember engagementMember = engagementMemberMapper.engagementMemberDTOToEngagementMember(engagementMemberDTO);
        engagementMember = engagementMemberRepository.save(engagementMember);
        EngagementMemberDTO result = engagementMemberMapper.engagementMemberToEngagementMemberDTO(engagementMember);
        engagementMemberSearchRepository.save(engagementMember);
        return result;
    }

    /**
     *  Get all the engagementMembers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<EngagementMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EngagementMembers");
        Page<EngagementMember> result = engagementMemberRepository.findAll(pageable);
        return result.map(engagementMember -> engagementMemberMapper.engagementMemberToEngagementMemberDTO(engagementMember));
    }

    /**
     *  Get one engagementMember by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EngagementMemberDTO findOne(Long id) {
        log.debug("Request to get EngagementMember : {}", id);
        EngagementMember engagementMember = engagementMemberRepository.findOne(id);
        EngagementMemberDTO engagementMemberDTO = engagementMemberMapper.engagementMemberToEngagementMemberDTO(engagementMember);
        return engagementMemberDTO;
    }

    /**
     *  Delete the  engagementMember by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EngagementMember : {}", id);
        engagementMemberRepository.delete(id);
        engagementMemberSearchRepository.delete(id);
    }

    /**
     * Search for the engagementMember corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EngagementMemberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EngagementMembers for query {}", query);
        Page<EngagementMember> result = engagementMemberSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(engagementMember -> engagementMemberMapper.engagementMemberToEngagementMemberDTO(engagementMember));
    }
}
