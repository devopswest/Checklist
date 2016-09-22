package com.pwc.assurance.adc.service;

import com.pwc.assurance.adc.service.dto.UserProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing UserProfile.
 */
public interface UserProfileService {

    /**
     * Save a userProfile.
     *
     * @param userProfileDTO the entity to save
     * @return the persisted entity
     */
    UserProfileDTO save(UserProfileDTO userProfileDTO);

    /**
     *  Get all the userProfiles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserProfileDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" userProfile.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserProfileDTO findOne(Long id);

    /**
     *  Delete the "id" userProfile.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserProfileDTO> search(String query, Pageable pageable);
}
