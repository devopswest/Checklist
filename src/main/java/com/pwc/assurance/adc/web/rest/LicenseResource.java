package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.License;

import com.pwc.assurance.adc.repository.LicenseRepository;
import com.pwc.assurance.adc.repository.search.LicenseSearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing License.
 */
@RestController
@RequestMapping("/api")
public class LicenseResource {

    private final Logger log = LoggerFactory.getLogger(LicenseResource.class);
        
    @Inject
    private LicenseRepository licenseRepository;

    @Inject
    private LicenseSearchRepository licenseSearchRepository;

    /**
     * POST  /licenses : Create a new license.
     *
     * @param license the license to create
     * @return the ResponseEntity with status 201 (Created) and with body the new license, or with status 400 (Bad Request) if the license has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/licenses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<License> createLicense(@Valid @RequestBody License license) throws URISyntaxException {
        log.debug("REST request to save License : {}", license);
        if (license.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("license", "idexists", "A new license cannot already have an ID")).body(null);
        }
        License result = licenseRepository.save(license);
        licenseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/licenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("license", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /licenses : Updates an existing license.
     *
     * @param license the license to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated license,
     * or with status 400 (Bad Request) if the license is not valid,
     * or with status 500 (Internal Server Error) if the license couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/licenses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<License> updateLicense(@Valid @RequestBody License license) throws URISyntaxException {
        log.debug("REST request to update License : {}", license);
        if (license.getId() == null) {
            return createLicense(license);
        }
        License result = licenseRepository.save(license);
        licenseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("license", license.getId().toString()))
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
    public ResponseEntity<List<License>> getAllLicenses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Licenses");
        Page<License> page = licenseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/licenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /licenses/:id : get the "id" license.
     *
     * @param id the id of the license to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the license, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/licenses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<License> getLicense(@PathVariable Long id) {
        log.debug("REST request to get License : {}", id);
        License license = licenseRepository.findOne(id);
        return Optional.ofNullable(license)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /licenses/:id : delete the "id" license.
     *
     * @param id the id of the license to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/licenses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        log.debug("REST request to delete License : {}", id);
        licenseRepository.delete(id);
        licenseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("license", id.toString())).build();
    }

    /**
     * SEARCH  /_search/licenses?query=:query : search for the license corresponding
     * to the query.
     *
     * @param query the query of the license search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/licenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<License>> searchLicenses(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Licenses for query {}", query);
        Page<License> page = licenseSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/licenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
