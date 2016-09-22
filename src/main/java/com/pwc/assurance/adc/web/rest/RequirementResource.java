package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.RequirementService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.RequirementDTO;
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
 * REST controller for managing Requirement.
 */
@RestController
@RequestMapping("/api")
public class RequirementResource {

    private final Logger log = LoggerFactory.getLogger(RequirementResource.class);
        
    @Inject
    private RequirementService requirementService;

    /**
     * POST  /requirements : Create a new requirement.
     *
     * @param requirementDTO the requirementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requirementDTO, or with status 400 (Bad Request) if the requirement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/requirements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RequirementDTO> createRequirement(@RequestBody RequirementDTO requirementDTO) throws URISyntaxException {
        log.debug("REST request to save Requirement : {}", requirementDTO);
        if (requirementDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("requirement", "idexists", "A new requirement cannot already have an ID")).body(null);
        }
        RequirementDTO result = requirementService.save(requirementDTO);
        return ResponseEntity.created(new URI("/api/requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("requirement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requirements : Updates an existing requirement.
     *
     * @param requirementDTO the requirementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requirementDTO,
     * or with status 400 (Bad Request) if the requirementDTO is not valid,
     * or with status 500 (Internal Server Error) if the requirementDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/requirements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RequirementDTO> updateRequirement(@RequestBody RequirementDTO requirementDTO) throws URISyntaxException {
        log.debug("REST request to update Requirement : {}", requirementDTO);
        if (requirementDTO.getId() == null) {
            return createRequirement(requirementDTO);
        }
        RequirementDTO result = requirementService.save(requirementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("requirement", requirementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requirements : get all the requirements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of requirements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/requirements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RequirementDTO>> getAllRequirements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Requirements");
        Page<RequirementDTO> page = requirementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requirements/:id : get the "id" requirement.
     *
     * @param id the id of the requirementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requirementDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/requirements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RequirementDTO> getRequirement(@PathVariable Long id) {
        log.debug("REST request to get Requirement : {}", id);
        RequirementDTO requirementDTO = requirementService.findOne(id);
        return Optional.ofNullable(requirementDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /requirements/:id : delete the "id" requirement.
     *
     * @param id the id of the requirementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/requirements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        log.debug("REST request to delete Requirement : {}", id);
        requirementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("requirement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/requirements?query=:query : search for the requirement corresponding
     * to the query.
     *
     * @param query the query of the requirement search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/requirements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RequirementDTO>> searchRequirements(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Requirements for query {}", query);
        Page<RequirementDTO> page = requirementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
