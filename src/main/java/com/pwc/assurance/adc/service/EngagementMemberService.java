package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.EngagementMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing EngagementMember.
 */
public interface EngagementMemberService {

    /**
     * Save a engagementMember.
     *
     * @param engagementMemberDTO the entity to save
     * @return the persisted entity
     */
    EngagementMemberDTO save(EngagementMemberDTO engagementMemberDTO);

    /**
     *  Get all the engagementMembers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EngagementMemberDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" engagementMember.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EngagementMemberDTO findOne(Long id);

    /**
     *  Delete the "id" engagementMember.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the engagementMember corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EngagementMemberDTO> search(String query, Pageable pageable);
}
