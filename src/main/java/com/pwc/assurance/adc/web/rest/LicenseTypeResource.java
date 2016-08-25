package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.LicenseType;

import com.pwc.assurance.adc.repository.LicenseTypeRepository;
import com.pwc.assurance.adc.repository.search.LicenseTypeSearchRepository;
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
 * REST controller for managing LicenseType.
 */
@RestController
@RequestMapping("/api")
public class LicenseTypeResource {

    private final Logger log = LoggerFactory.getLogger(LicenseTypeResource.class);
        
    @Inject
    private LicenseTypeRepository licenseTypeRepository;

    @Inject
    private LicenseTypeSearchRepository licenseTypeSearchRepository;

    /**
     * POST  /license-types : Create a new licenseType.
     *
     * @param licenseType the licenseType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new licenseType, or with status 400 (Bad Request) if the licenseType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/license-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LicenseType> createLicenseType(@RequestBody LicenseType licenseType) throws URISyntaxException {
        log.debug("REST request to save LicenseType : {}", licenseType);
        if (licenseType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("licenseType", "idexists", "A new licenseType cannot already have an ID")).body(null);
        }
        LicenseType result = licenseTypeRepository.save(licenseType);
        licenseTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/license-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("licenseType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /license-types : Updates an existing licenseType.
     *
     * @param licenseType the licenseType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated licenseType,
     * or with status 400 (Bad Request) if the licenseType is not valid,
     * or with status 500 (Internal Server Error) if the licenseType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/license-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LicenseType> updateLicenseType(@RequestBody LicenseType licenseType) throws URISyntaxException {
        log.debug("REST request to update LicenseType : {}", licenseType);
        if (licenseType.getId() == null) {
            return createLicenseType(licenseType);
        }
        LicenseType result = licenseTypeRepository.save(licenseType);
        licenseTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("licenseType", licenseType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /license-types : get all the licenseTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of licenseTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/license-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LicenseType>> getAllLicenseTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LicenseTypes");
        Page<LicenseType> page = licenseTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/license-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /license-types/:id : get the "id" licenseType.
     *
     * @param id the id of the licenseType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the licenseType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/license-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LicenseType> getLicenseType(@PathVariable Long id) {
        log.debug("REST request to get LicenseType : {}", id);
        LicenseType licenseType = licenseTypeRepository.findOne(id);
        return Optional.ofNullable(licenseType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /license-types/:id : delete the "id" licenseType.
     *
     * @param id the id of the licenseType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/license-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLicenseType(@PathVariable Long id) {
        log.debug("REST request to delete LicenseType : {}", id);
        licenseTypeRepository.delete(id);
        licenseTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("licenseType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/license-types?query=:query : search for the licenseType corresponding
     * to the query.
     *
     * @param query the query of the licenseType search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/license-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LicenseType>> searchLicenseTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LicenseTypes for query {}", query);
        Page<LicenseType> page = licenseTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/license-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
