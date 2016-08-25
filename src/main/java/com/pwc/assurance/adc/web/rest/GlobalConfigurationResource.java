package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.GlobalConfiguration;

import com.pwc.assurance.adc.repository.GlobalConfigurationRepository;
import com.pwc.assurance.adc.repository.search.GlobalConfigurationSearchRepository;
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
 * REST controller for managing GlobalConfiguration.
 */
@RestController
@RequestMapping("/api")
public class GlobalConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigurationResource.class);
        
    @Inject
    private GlobalConfigurationRepository globalConfigurationRepository;

    @Inject
    private GlobalConfigurationSearchRepository globalConfigurationSearchRepository;

    /**
     * POST  /global-configurations : Create a new globalConfiguration.
     *
     * @param globalConfiguration the globalConfiguration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new globalConfiguration, or with status 400 (Bad Request) if the globalConfiguration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/global-configurations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GlobalConfiguration> createGlobalConfiguration(@RequestBody GlobalConfiguration globalConfiguration) throws URISyntaxException {
        log.debug("REST request to save GlobalConfiguration : {}", globalConfiguration);
        if (globalConfiguration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("globalConfiguration", "idexists", "A new globalConfiguration cannot already have an ID")).body(null);
        }
        GlobalConfiguration result = globalConfigurationRepository.save(globalConfiguration);
        globalConfigurationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/global-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("globalConfiguration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /global-configurations : Updates an existing globalConfiguration.
     *
     * @param globalConfiguration the globalConfiguration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated globalConfiguration,
     * or with status 400 (Bad Request) if the globalConfiguration is not valid,
     * or with status 500 (Internal Server Error) if the globalConfiguration couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/global-configurations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GlobalConfiguration> updateGlobalConfiguration(@RequestBody GlobalConfiguration globalConfiguration) throws URISyntaxException {
        log.debug("REST request to update GlobalConfiguration : {}", globalConfiguration);
        if (globalConfiguration.getId() == null) {
            return createGlobalConfiguration(globalConfiguration);
        }
        GlobalConfiguration result = globalConfigurationRepository.save(globalConfiguration);
        globalConfigurationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("globalConfiguration", globalConfiguration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /global-configurations : get all the globalConfigurations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of globalConfigurations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/global-configurations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GlobalConfiguration>> getAllGlobalConfigurations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GlobalConfigurations");
        Page<GlobalConfiguration> page = globalConfigurationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/global-configurations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /global-configurations/:id : get the "id" globalConfiguration.
     *
     * @param id the id of the globalConfiguration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the globalConfiguration, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/global-configurations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GlobalConfiguration> getGlobalConfiguration(@PathVariable Long id) {
        log.debug("REST request to get GlobalConfiguration : {}", id);
        GlobalConfiguration globalConfiguration = globalConfigurationRepository.findOne(id);
        return Optional.ofNullable(globalConfiguration)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /global-configurations/:id : delete the "id" globalConfiguration.
     *
     * @param id the id of the globalConfiguration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/global-configurations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGlobalConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete GlobalConfiguration : {}", id);
        globalConfigurationRepository.delete(id);
        globalConfigurationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("globalConfiguration", id.toString())).build();
    }

    /**
     * SEARCH  /_search/global-configurations?query=:query : search for the globalConfiguration corresponding
     * to the query.
     *
     * @param query the query of the globalConfiguration search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/global-configurations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GlobalConfiguration>> searchGlobalConfigurations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of GlobalConfigurations for query {}", query);
        Page<GlobalConfiguration> page = globalConfigurationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/global-configurations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
