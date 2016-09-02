package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.EngagementMember;

import com.pwc.assurance.adc.repository.EngagementMemberRepository;
import com.pwc.assurance.adc.repository.search.EngagementMemberSearchRepository;
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
 * REST controller for managing EngagementMember.
 */
@RestController
@RequestMapping("/api")
public class EngagementMemberResource {

    private final Logger log = LoggerFactory.getLogger(EngagementMemberResource.class);
        
    @Inject
    private EngagementMemberRepository engagementMemberRepository;

    @Inject
    private EngagementMemberSearchRepository engagementMemberSearchRepository;

    /**
     * POST  /engagement-members : Create a new engagementMember.
     *
     * @param engagementMember the engagementMember to create
     * @return the ResponseEntity with status 201 (Created) and with body the new engagementMember, or with status 400 (Bad Request) if the engagementMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagement-members",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementMember> createEngagementMember(@RequestBody EngagementMember engagementMember) throws URISyntaxException {
        log.debug("REST request to save EngagementMember : {}", engagementMember);
        if (engagementMember.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("engagementMember", "idexists", "A new engagementMember cannot already have an ID")).body(null);
        }
        EngagementMember result = engagementMemberRepository.save(engagementMember);
        engagementMemberSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/engagement-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("engagementMember", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /engagement-members : Updates an existing engagementMember.
     *
     * @param engagementMember the engagementMember to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated engagementMember,
     * or with status 400 (Bad Request) if the engagementMember is not valid,
     * or with status 500 (Internal Server Error) if the engagementMember couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagement-members",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementMember> updateEngagementMember(@RequestBody EngagementMember engagementMember) throws URISyntaxException {
        log.debug("REST request to update EngagementMember : {}", engagementMember);
        if (engagementMember.getId() == null) {
            return createEngagementMember(engagementMember);
        }
        EngagementMember result = engagementMemberRepository.save(engagementMember);
        engagementMemberSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("engagementMember", engagementMember.getId().toString()))
            .body(result);
    }

    /**
     * GET  /engagement-members : get all the engagementMembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of engagementMembers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/engagement-members",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EngagementMember>> getAllEngagementMembers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EngagementMembers");
        Page<EngagementMember> page = engagementMemberRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/engagement-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /engagement-members/:id : get the "id" engagementMember.
     *
     * @param id the id of the engagementMember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the engagementMember, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/engagement-members/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementMember> getEngagementMember(@PathVariable Long id) {
        log.debug("REST request to get EngagementMember : {}", id);
        EngagementMember engagementMember = engagementMemberRepository.findOne(id);
        return Optional.ofNullable(engagementMember)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /engagement-members/:id : delete the "id" engagementMember.
     *
     * @param id the id of the engagementMember to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/engagement-members/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEngagementMember(@PathVariable Long id) {
        log.debug("REST request to delete EngagementMember : {}", id);
        engagementMemberRepository.delete(id);
        engagementMemberSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("engagementMember", id.toString())).build();
    }

    /**
     * SEARCH  /_search/engagement-members?query=:query : search for the engagementMember corresponding
     * to the query.
     *
     * @param query the query of the engagementMember search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/engagement-members",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EngagementMember>> searchEngagementMembers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of EngagementMembers for query {}", query);
        Page<EngagementMember> page = engagementMemberSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/engagement-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
