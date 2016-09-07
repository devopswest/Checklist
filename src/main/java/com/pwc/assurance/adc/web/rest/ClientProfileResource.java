package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.ClientProfile;

import com.pwc.assurance.adc.repository.ClientProfileRepository;
import com.pwc.assurance.adc.repository.search.ClientProfileSearchRepository;
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
 * REST controller for managing ClientProfile.
 */
@RestController
@RequestMapping("/api")
public class ClientProfileResource {

    private final Logger log = LoggerFactory.getLogger(ClientProfileResource.class);
        
    @Inject
    private ClientProfileRepository clientProfileRepository;

    @Inject
    private ClientProfileSearchRepository clientProfileSearchRepository;

    /**
     * POST  /client-profiles : Create a new clientProfile.
     *
     * @param clientProfile the clientProfile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientProfile, or with status 400 (Bad Request) if the clientProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-profiles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientProfile> createClientProfile(@RequestBody ClientProfile clientProfile) throws URISyntaxException {
        log.debug("REST request to save ClientProfile : {}", clientProfile);
        if (clientProfile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientProfile", "idexists", "A new clientProfile cannot already have an ID")).body(null);
        }
        ClientProfile result = clientProfileRepository.save(clientProfile);
        clientProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/client-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientProfile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-profiles : Updates an existing clientProfile.
     *
     * @param clientProfile the clientProfile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientProfile,
     * or with status 400 (Bad Request) if the clientProfile is not valid,
     * or with status 500 (Internal Server Error) if the clientProfile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-profiles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientProfile> updateClientProfile(@RequestBody ClientProfile clientProfile) throws URISyntaxException {
        log.debug("REST request to update ClientProfile : {}", clientProfile);
        if (clientProfile.getId() == null) {
            return createClientProfile(clientProfile);
        }
        ClientProfile result = clientProfileRepository.save(clientProfile);
        clientProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientProfile", clientProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-profiles : get all the clientProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clientProfiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/client-profiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientProfile>> getAllClientProfiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ClientProfiles");
        Page<ClientProfile> page = clientProfileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/client-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /client-profiles/:id : get the "id" clientProfile.
     *
     * @param id the id of the clientProfile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientProfile, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-profiles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientProfile> getClientProfile(@PathVariable Long id) {
        log.debug("REST request to get ClientProfile : {}", id);
        ClientProfile clientProfile = clientProfileRepository.findOne(id);
        return Optional.ofNullable(clientProfile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-profiles/:id : delete the "id" clientProfile.
     *
     * @param id the id of the clientProfile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-profiles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientProfile(@PathVariable Long id) {
        log.debug("REST request to delete ClientProfile : {}", id);
        clientProfileRepository.delete(id);
        clientProfileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientProfile", id.toString())).build();
    }

    /**
     * SEARCH  /_search/client-profiles?query=:query : search for the clientProfile corresponding
     * to the query.
     *
     * @param query the query of the clientProfile search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/client-profiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientProfile>> searchClientProfiles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ClientProfiles for query {}", query);
        Page<ClientProfile> page = clientProfileSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/client-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
