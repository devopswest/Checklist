package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.UserProfile;

import com.pwc.assurance.adc.repository.UserProfileRepository;
import com.pwc.assurance.adc.repository.search.UserProfileSearchRepository;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing UserProfile.
 */
@RestController
@RequestMapping("/api")
public class UserProfileResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);
        
    @Inject
    private UserProfileRepository userProfileRepository;

    @Inject
    private UserProfileSearchRepository userProfileSearchRepository;

    /**
     * POST  /user-profiles : Create a new userProfile.
     *
     * @param userProfile the userProfile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userProfile, or with status 400 (Bad Request) if the userProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-profiles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to save UserProfile : {}", userProfile);
        if (userProfile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userProfile", "idexists", "A new userProfile cannot already have an ID")).body(null);
        }
        UserProfile result = userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userProfile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-profiles : Updates an existing userProfile.
     *
     * @param userProfile the userProfile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userProfile,
     * or with status 400 (Bad Request) if the userProfile is not valid,
     * or with status 500 (Internal Server Error) if the userProfile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-profiles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to update UserProfile : {}", userProfile);
        if (userProfile.getId() == null) {
            return createUserProfile(userProfile);
        }
        UserProfile result = userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userProfile", userProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-profiles : get all the userProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userProfiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/user-profiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserProfile>> getAllUserProfiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserProfiles");
        Page<UserProfile> page = userProfileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-profiles/:id : get the "id" userProfile.
     *
     * @param id the id of the userProfile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userProfile, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-profiles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        UserProfile userProfile = userProfileRepository.findOne(id);
        return Optional.ofNullable(userProfile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-profiles/:id : delete the "id" userProfile.
     *
     * @param id the id of the userProfile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-profiles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        userProfileRepository.delete(id);
        userProfileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userProfile", id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-profiles?query=:query : search for the userProfile corresponding
     * to the query.
     *
     * @param query the query of the userProfile search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/user-profiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserProfile>> searchUserProfiles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of UserProfiles for query {}", query);
        Page<UserProfile> page = userProfileSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
