package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.ClientMetadata;

import com.pwc.assurance.adc.repository.ClientMetadataRepository;
import com.pwc.assurance.adc.repository.search.ClientMetadataSearchRepository;
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
 * REST controller for managing ClientMetadata.
 */
@RestController
@RequestMapping("/api")
public class ClientMetadataResource {

    private final Logger log = LoggerFactory.getLogger(ClientMetadataResource.class);
        
    @Inject
    private ClientMetadataRepository clientMetadataRepository;

    @Inject
    private ClientMetadataSearchRepository clientMetadataSearchRepository;

    /**
     * POST  /client-metadata : Create a new clientMetadata.
     *
     * @param clientMetadata the clientMetadata to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientMetadata, or with status 400 (Bad Request) if the clientMetadata has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-metadata",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientMetadata> createClientMetadata(@RequestBody ClientMetadata clientMetadata) throws URISyntaxException {
        log.debug("REST request to save ClientMetadata : {}", clientMetadata);
        if (clientMetadata.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientMetadata", "idexists", "A new clientMetadata cannot already have an ID")).body(null);
        }
        ClientMetadata result = clientMetadataRepository.save(clientMetadata);
        clientMetadataSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/client-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientMetadata", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-metadata : Updates an existing clientMetadata.
     *
     * @param clientMetadata the clientMetadata to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientMetadata,
     * or with status 400 (Bad Request) if the clientMetadata is not valid,
     * or with status 500 (Internal Server Error) if the clientMetadata couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-metadata",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientMetadata> updateClientMetadata(@RequestBody ClientMetadata clientMetadata) throws URISyntaxException {
        log.debug("REST request to update ClientMetadata : {}", clientMetadata);
        if (clientMetadata.getId() == null) {
            return createClientMetadata(clientMetadata);
        }
        ClientMetadata result = clientMetadataRepository.save(clientMetadata);
        clientMetadataSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientMetadata", clientMetadata.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-metadata : get all the clientMetadata.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clientMetadata in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/client-metadata",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientMetadata>> getAllClientMetadata(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ClientMetadata");
        Page<ClientMetadata> page = clientMetadataRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/client-metadata");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /client-metadata/:id : get the "id" clientMetadata.
     *
     * @param id the id of the clientMetadata to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientMetadata, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-metadata/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientMetadata> getClientMetadata(@PathVariable Long id) {
        log.debug("REST request to get ClientMetadata : {}", id);
        ClientMetadata clientMetadata = clientMetadataRepository.findOne(id);
        return Optional.ofNullable(clientMetadata)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-metadata/:id : delete the "id" clientMetadata.
     *
     * @param id the id of the clientMetadata to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-metadata/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientMetadata(@PathVariable Long id) {
        log.debug("REST request to delete ClientMetadata : {}", id);
        clientMetadataRepository.delete(id);
        clientMetadataSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientMetadata", id.toString())).build();
    }

    /**
     * SEARCH  /_search/client-metadata?query=:query : search for the clientMetadata corresponding
     * to the query.
     *
     * @param query the query of the clientMetadata search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/client-metadata",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientMetadata>> searchClientMetadata(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ClientMetadata for query {}", query);
        Page<ClientMetadata> page = clientMetadataSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/client-metadata");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
