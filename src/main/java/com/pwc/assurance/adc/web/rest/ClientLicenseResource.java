package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.ClientLicenseService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.ClientLicenseDTO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ClientLicense.
 */
@RestController
@RequestMapping("/api")
public class ClientLicenseResource {

    private final Logger log = LoggerFactory.getLogger(ClientLicenseResource.class);
        
    @Inject
    private ClientLicenseService clientLicenseService;

    /**
     * POST  /client-licenses : Create a new clientLicense.
     *
     * @param clientLicenseDTO the clientLicenseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientLicenseDTO, or with status 400 (Bad Request) if the clientLicense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-licenses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientLicenseDTO> createClientLicense(@Valid @RequestBody ClientLicenseDTO clientLicenseDTO) throws URISyntaxException {
        log.debug("REST request to save ClientLicense : {}", clientLicenseDTO);
        if (clientLicenseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientLicense", "idexists", "A new clientLicense cannot already have an ID")).body(null);
        }
        ClientLicenseDTO result = clientLicenseService.save(clientLicenseDTO);
        return ResponseEntity.created(new URI("/api/client-licenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientLicense", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-licenses : Updates an existing clientLicense.
     *
     * @param clientLicenseDTO the clientLicenseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientLicenseDTO,
     * or with status 400 (Bad Request) if the clientLicenseDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientLicenseDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-licenses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientLicenseDTO> updateClientLicense(@Valid @RequestBody ClientLicenseDTO clientLicenseDTO) throws URISyntaxException {
        log.debug("REST request to update ClientLicense : {}", clientLicenseDTO);
        if (clientLicenseDTO.getId() == null) {
            return createClientLicense(clientLicenseDTO);
        }
        ClientLicenseDTO result = clientLicenseService.save(clientLicenseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientLicense", clientLicenseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-licenses : get all the clientLicenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clientLicenses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/client-licenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientLicenseDTO>> getAllClientLicenses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ClientLicenses");
        Page<ClientLicenseDTO> page = clientLicenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/client-licenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /client-licenses/:id : get the "id" clientLicense.
     *
     * @param id the id of the clientLicenseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientLicenseDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-licenses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientLicenseDTO> getClientLicense(@PathVariable Long id) {
        log.debug("REST request to get ClientLicense : {}", id);
        ClientLicenseDTO clientLicenseDTO = clientLicenseService.findOne(id);
        return Optional.ofNullable(clientLicenseDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-licenses/:id : delete the "id" clientLicense.
     *
     * @param id the id of the clientLicenseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-licenses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientLicense(@PathVariable Long id) {
        log.debug("REST request to delete ClientLicense : {}", id);
        clientLicenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientLicense", id.toString())).build();
    }

    /**
     * SEARCH  /_search/client-licenses?query=:query : search for the clientLicense corresponding
     * to the query.
     *
     * @param query the query of the clientLicense search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/client-licenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientLicenseDTO>> searchClientLicenses(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ClientLicenses for query {}", query);
        Page<ClientLicenseDTO> page = clientLicenseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/client-licenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
