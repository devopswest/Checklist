package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.Licenses;

import com.pwc.assurance.adc.repository.LicensesRepository;
import com.pwc.assurance.adc.repository.search.LicensesSearchRepository;
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
 * REST controller for managing Licenses.
 */
@RestController
@RequestMapping("/api")
public class LicensesResource {

    private final Logger log = LoggerFactory.getLogger(LicensesResource.class);
        
    @Inject
    private LicensesRepository licensesRepository;

    @Inject
    private LicensesSearchRepository licensesSearchRepository;

    /**
     * POST  /licenses : Create a new licenses.
     *
     * @param licenses the licenses to create
     * @return the ResponseEntity with status 201 (Created) and with body the new licenses, or with status 400 (Bad Request) if the licenses has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/licenses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Licenses> createLicenses(@RequestBody Licenses licenses) throws URISyntaxException {
        log.debug("REST request to save Licenses : {}", licenses);
        if (licenses.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("licenses", "idexists", "A new licenses cannot already have an ID")).body(null);
        }
        Licenses result = licensesRepository.save(licenses);
        licensesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/licenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("licenses", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /licenses : Updates an existing licenses.
     *
     * @param licenses the licenses to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated licenses,
     * or with status 400 (Bad Request) if the licenses is not valid,
     * or with status 500 (Internal Server Error) if the licenses couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/licenses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Licenses> updateLicenses(@RequestBody Licenses licenses) throws URISyntaxException {
        log.debug("REST request to update Licenses : {}", licenses);
        if (licenses.getId() == null) {
            return createLicenses(licenses);
        }
        Licenses result = licensesRepository.save(licenses);
        licensesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("licenses", licenses.getId().toString()))
            .body(result);
    }

    /**
     * GET  /licenses : get all the licenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of licenses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/licenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Licenses>> getAllLicenses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Licenses");
        Page<Licenses> page = licensesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/licenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /licenses/:id : get the "id" licenses.
     *
     * @param id the id of the licenses to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the licenses, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/licenses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Licenses> getLicenses(@PathVariable Long id) {
        log.debug("REST request to get Licenses : {}", id);
        Licenses licenses = licensesRepository.findOne(id);
        return Optional.ofNullable(licenses)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /licenses/:id : delete the "id" licenses.
     *
     * @param id the id of the licenses to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/licenses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLicenses(@PathVariable Long id) {
        log.debug("REST request to delete Licenses : {}", id);
        licensesRepository.delete(id);
        licensesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("licenses", id.toString())).build();
    }

    /**
     * SEARCH  /_search/licenses?query=:query : search for the licenses corresponding
     * to the query.
     *
     * @param query the query of the licenses search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/licenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Licenses>> searchLicenses(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Licenses for query {}", query);
        Page<Licenses> page = licensesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/licenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
