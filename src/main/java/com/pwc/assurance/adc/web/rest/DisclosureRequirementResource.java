package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.DisclosureRequirementService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementDTO;
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
 * REST controller for managing DisclosureRequirement.
 */
@RestController
@RequestMapping("/api")
public class DisclosureRequirementResource {

    private final Logger log = LoggerFactory.getLogger(DisclosureRequirementResource.class);
        
    @Inject
    private DisclosureRequirementService disclosureRequirementService;

    /**
     * POST  /disclosure-requirements : Create a new disclosureRequirement.
     *
     * @param disclosureRequirementDTO the disclosureRequirementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new disclosureRequirementDTO, or with status 400 (Bad Request) if the disclosureRequirement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/disclosure-requirements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisclosureRequirementDTO> createDisclosureRequirement(@Valid @RequestBody DisclosureRequirementDTO disclosureRequirementDTO) throws URISyntaxException {
        log.debug("REST request to save DisclosureRequirement : {}", disclosureRequirementDTO);
        if (disclosureRequirementDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("disclosureRequirement", "idexists", "A new disclosureRequirement cannot already have an ID")).body(null);
        }
        DisclosureRequirementDTO result = disclosureRequirementService.save(disclosureRequirementDTO);
        return ResponseEntity.created(new URI("/api/disclosure-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("disclosureRequirement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disclosure-requirements : Updates an existing disclosureRequirement.
     *
     * @param disclosureRequirementDTO the disclosureRequirementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated disclosureRequirementDTO,
     * or with status 400 (Bad Request) if the disclosureRequirementDTO is not valid,
     * or with status 500 (Internal Server Error) if the disclosureRequirementDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/disclosure-requirements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisclosureRequirementDTO> updateDisclosureRequirement(@Valid @RequestBody DisclosureRequirementDTO disclosureRequirementDTO) throws URISyntaxException {
        log.debug("REST request to update DisclosureRequirement : {}", disclosureRequirementDTO);
        if (disclosureRequirementDTO.getId() == null) {
            return createDisclosureRequirement(disclosureRequirementDTO);
        }
        DisclosureRequirementDTO result = disclosureRequirementService.save(disclosureRequirementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("disclosureRequirement", disclosureRequirementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disclosure-requirements : get all the disclosureRequirements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of disclosureRequirements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/disclosure-requirements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DisclosureRequirementDTO>> getAllDisclosureRequirements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DisclosureRequirements");
        Page<DisclosureRequirementDTO> page = disclosureRequirementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/disclosure-requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /disclosure-requirements/:id : get the "id" disclosureRequirement.
     *
     * @param id the id of the disclosureRequirementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the disclosureRequirementDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/disclosure-requirements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisclosureRequirementDTO> getDisclosureRequirement(@PathVariable Long id) {
        log.debug("REST request to get DisclosureRequirement : {}", id);
        DisclosureRequirementDTO disclosureRequirementDTO = disclosureRequirementService.findOne(id);
        return Optional.ofNullable(disclosureRequirementDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /disclosure-requirements/:id : delete the "id" disclosureRequirement.
     *
     * @param id the id of the disclosureRequirementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/disclosure-requirements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDisclosureRequirement(@PathVariable Long id) {
        log.debug("REST request to delete DisclosureRequirement : {}", id);
        disclosureRequirementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("disclosureRequirement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/disclosure-requirements?query=:query : search for the disclosureRequirement corresponding
     * to the query.
     *
     * @param query the query of the disclosureRequirement search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/disclosure-requirements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DisclosureRequirementDTO>> searchDisclosureRequirements(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DisclosureRequirements for query {}", query);
        Page<DisclosureRequirementDTO> page = disclosureRequirementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/disclosure-requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
