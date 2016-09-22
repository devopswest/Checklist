package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.EngagementMemberService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.EngagementMemberDTO;
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
import java.util.LinkedList;
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
    private EngagementMemberService engagementMemberService;

    /**
     * POST  /engagement-members : Create a new engagementMember.
     *
     * @param engagementMemberDTO the engagementMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new engagementMemberDTO, or with status 400 (Bad Request) if the engagementMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagement-members",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementMemberDTO> createEngagementMember(@RequestBody EngagementMemberDTO engagementMemberDTO) throws URISyntaxException {
        log.debug("REST request to save EngagementMember : {}", engagementMemberDTO);
        if (engagementMemberDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("engagementMember", "idexists", "A new engagementMember cannot already have an ID")).body(null);
        }
        EngagementMemberDTO result = engagementMemberService.save(engagementMemberDTO);
        return ResponseEntity.created(new URI("/api/engagement-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("engagementMember", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /engagement-members : Updates an existing engagementMember.
     *
     * @param engagementMemberDTO the engagementMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated engagementMemberDTO,
     * or with status 400 (Bad Request) if the engagementMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the engagementMemberDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagement-members",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementMemberDTO> updateEngagementMember(@RequestBody EngagementMemberDTO engagementMemberDTO) throws URISyntaxException {
        log.debug("REST request to update EngagementMember : {}", engagementMemberDTO);
        if (engagementMemberDTO.getId() == null) {
            return createEngagementMember(engagementMemberDTO);
        }
        EngagementMemberDTO result = engagementMemberService.save(engagementMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("engagementMember", engagementMemberDTO.getId().toString()))
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
    public ResponseEntity<List<EngagementMemberDTO>> getAllEngagementMembers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EngagementMembers");
        Page<EngagementMemberDTO> page = engagementMemberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/engagement-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /engagement-members/:id : get the "id" engagementMember.
     *
     * @param id the id of the engagementMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the engagementMemberDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/engagement-members/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementMemberDTO> getEngagementMember(@PathVariable Long id) {
        log.debug("REST request to get EngagementMember : {}", id);
        EngagementMemberDTO engagementMemberDTO = engagementMemberService.findOne(id);
        return Optional.ofNullable(engagementMemberDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /engagement-members/:id : delete the "id" engagementMember.
     *
     * @param id the id of the engagementMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/engagement-members/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEngagementMember(@PathVariable Long id) {
        log.debug("REST request to delete EngagementMember : {}", id);
        engagementMemberService.delete(id);
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
    public ResponseEntity<List<EngagementMemberDTO>> searchEngagementMembers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of EngagementMembers for query {}", query);
        Page<EngagementMemberDTO> page = engagementMemberService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/engagement-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
