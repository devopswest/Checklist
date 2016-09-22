package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.ClientTagService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.ClientTagDTO;
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
 * REST controller for managing ClientTag.
 */
@RestController
@RequestMapping("/api")
public class ClientTagResource {

    private final Logger log = LoggerFactory.getLogger(ClientTagResource.class);
        
    @Inject
    private ClientTagService clientTagService;

    /**
     * POST  /client-tags : Create a new clientTag.
     *
     * @param clientTagDTO the clientTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientTagDTO, or with status 400 (Bad Request) if the clientTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-tags",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientTagDTO> createClientTag(@RequestBody ClientTagDTO clientTagDTO) throws URISyntaxException {
        log.debug("REST request to save ClientTag : {}", clientTagDTO);
        if (clientTagDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientTag", "idexists", "A new clientTag cannot already have an ID")).body(null);
        }
        ClientTagDTO result = clientTagService.save(clientTagDTO);
        return ResponseEntity.created(new URI("/api/client-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientTag", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-tags : Updates an existing clientTag.
     *
     * @param clientTagDTO the clientTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientTagDTO,
     * or with status 400 (Bad Request) if the clientTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientTagDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-tags",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientTagDTO> updateClientTag(@RequestBody ClientTagDTO clientTagDTO) throws URISyntaxException {
        log.debug("REST request to update ClientTag : {}", clientTagDTO);
        if (clientTagDTO.getId() == null) {
            return createClientTag(clientTagDTO);
        }
        ClientTagDTO result = clientTagService.save(clientTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientTag", clientTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-tags : get all the clientTags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clientTags in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/client-tags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientTagDTO>> getAllClientTags(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ClientTags");
        Page<ClientTagDTO> page = clientTagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/client-tags");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /client-tags/:id : get the "id" clientTag.
     *
     * @param id the id of the clientTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientTagDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-tags/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientTagDTO> getClientTag(@PathVariable Long id) {
        log.debug("REST request to get ClientTag : {}", id);
        ClientTagDTO clientTagDTO = clientTagService.findOne(id);
        return Optional.ofNullable(clientTagDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-tags/:id : delete the "id" clientTag.
     *
     * @param id the id of the clientTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-tags/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientTag(@PathVariable Long id) {
        log.debug("REST request to delete ClientTag : {}", id);
        clientTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientTag", id.toString())).build();
    }

    /**
     * SEARCH  /_search/client-tags?query=:query : search for the clientTag corresponding
     * to the query.
     *
     * @param query the query of the clientTag search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/client-tags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientTagDTO>> searchClientTags(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ClientTags for query {}", query);
        Page<ClientTagDTO> page = clientTagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/client-tags");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
