package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.Engagement;

import com.pwc.assurance.adc.repository.EngagementRepository;
import com.pwc.assurance.adc.repository.search.EngagementSearchRepository;
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
 * REST controller for managing Engagement.
 */
@RestController
@RequestMapping("/api")
public class EngagementResource {

    private final Logger log = LoggerFactory.getLogger(EngagementResource.class);
        
    @Inject
    private EngagementRepository engagementRepository;

    @Inject
    private EngagementSearchRepository engagementSearchRepository;

    /**
     * POST  /engagements : Create a new engagement.
     *
     * @param engagement the engagement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new engagement, or with status 400 (Bad Request) if the engagement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Engagement> createEngagement(@RequestBody Engagement engagement) throws URISyntaxException {
        log.debug("REST request to save Engagement : {}", engagement);
        if (engagement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("engagement", "idexists", "A new engagement cannot already have an ID")).body(null);
        }
        Engagement result = engagementRepository.save(engagement);
        engagementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/engagements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("engagement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /engagements : Updates an existing engagement.
     *
     * @param engagement the engagement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated engagement,
     * or with status 400 (Bad Request) if the engagement is not valid,
     * or with status 500 (Internal Server Error) if the engagement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Engagement> updateEngagement(@RequestBody Engagement engagement) throws URISyntaxException {
        log.debug("REST request to update Engagement : {}", engagement);
        if (engagement.getId() == null) {
            return createEngagement(engagement);
        }
        Engagement result = engagementRepository.save(engagement);
        engagementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("engagement", engagement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /engagements : get all the engagements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of engagements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/engagements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Engagement>> getAllEngagements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Engagements");
        Page<Engagement> page = engagementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/engagements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /engagements/:id : get the "id" engagement.
     *
     * @param id the id of the engagement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the engagement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/engagements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Engagement> getEngagement(@PathVariable Long id) {
        log.debug("REST request to get Engagement : {}", id);
        Engagement engagement = engagementRepository.findOne(id);
        return Optional.ofNullable(engagement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /engagements/:id : delete the "id" engagement.
     *
     * @param id the id of the engagement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/engagements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEngagement(@PathVariable Long id) {
        log.debug("REST request to delete Engagement : {}", id);
        engagementRepository.delete(id);
        engagementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("engagement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/engagements?query=:query : search for the engagement corresponding
     * to the query.
     *
     * @param query the query of the engagement search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/engagements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Engagement>> searchEngagements(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Engagements for query {}", query);
        Page<Engagement> page = engagementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/engagements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
