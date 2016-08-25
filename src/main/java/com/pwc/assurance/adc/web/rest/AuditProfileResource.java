package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.AuditProfile;

import com.pwc.assurance.adc.repository.AuditProfileRepository;
import com.pwc.assurance.adc.repository.search.AuditProfileSearchRepository;
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
 * REST controller for managing AuditProfile.
 */
@RestController
@RequestMapping("/api")
public class AuditProfileResource {

    private final Logger log = LoggerFactory.getLogger(AuditProfileResource.class);
        
    @Inject
    private AuditProfileRepository auditProfileRepository;

    @Inject
    private AuditProfileSearchRepository auditProfileSearchRepository;

    /**
     * POST  /audit-profiles : Create a new auditProfile.
     *
     * @param auditProfile the auditProfile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auditProfile, or with status 400 (Bad Request) if the auditProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audit-profiles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditProfile> createAuditProfile(@RequestBody AuditProfile auditProfile) throws URISyntaxException {
        log.debug("REST request to save AuditProfile : {}", auditProfile);
        if (auditProfile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auditProfile", "idexists", "A new auditProfile cannot already have an ID")).body(null);
        }
        AuditProfile result = auditProfileRepository.save(auditProfile);
        auditProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/audit-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("auditProfile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audit-profiles : Updates an existing auditProfile.
     *
     * @param auditProfile the auditProfile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auditProfile,
     * or with status 400 (Bad Request) if the auditProfile is not valid,
     * or with status 500 (Internal Server Error) if the auditProfile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audit-profiles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditProfile> updateAuditProfile(@RequestBody AuditProfile auditProfile) throws URISyntaxException {
        log.debug("REST request to update AuditProfile : {}", auditProfile);
        if (auditProfile.getId() == null) {
            return createAuditProfile(auditProfile);
        }
        AuditProfile result = auditProfileRepository.save(auditProfile);
        auditProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("auditProfile", auditProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audit-profiles : get all the auditProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of auditProfiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/audit-profiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuditProfile>> getAllAuditProfiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AuditProfiles");
        Page<AuditProfile> page = auditProfileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/audit-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /audit-profiles/:id : get the "id" auditProfile.
     *
     * @param id the id of the auditProfile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auditProfile, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/audit-profiles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditProfile> getAuditProfile(@PathVariable Long id) {
        log.debug("REST request to get AuditProfile : {}", id);
        AuditProfile auditProfile = auditProfileRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(auditProfile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /audit-profiles/:id : delete the "id" auditProfile.
     *
     * @param id the id of the auditProfile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/audit-profiles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAuditProfile(@PathVariable Long id) {
        log.debug("REST request to delete AuditProfile : {}", id);
        auditProfileRepository.delete(id);
        auditProfileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("auditProfile", id.toString())).build();
    }

    /**
     * SEARCH  /_search/audit-profiles?query=:query : search for the auditProfile corresponding
     * to the query.
     *
     * @param query the query of the auditProfile search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/audit-profiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuditProfile>> searchAuditProfiles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AuditProfiles for query {}", query);
        Page<AuditProfile> page = auditProfileSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/audit-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
