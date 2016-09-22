package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.ChecklistWorkflowService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.ChecklistWorkflowDTO;
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
 * REST controller for managing ChecklistWorkflow.
 */
@RestController
@RequestMapping("/api")
public class ChecklistWorkflowResource {

    private final Logger log = LoggerFactory.getLogger(ChecklistWorkflowResource.class);
        
    @Inject
    private ChecklistWorkflowService checklistWorkflowService;

    /**
     * POST  /checklist-workflows : Create a new checklistWorkflow.
     *
     * @param checklistWorkflowDTO the checklistWorkflowDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklistWorkflowDTO, or with status 400 (Bad Request) if the checklistWorkflow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-workflows",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistWorkflowDTO> createChecklistWorkflow(@RequestBody ChecklistWorkflowDTO checklistWorkflowDTO) throws URISyntaxException {
        log.debug("REST request to save ChecklistWorkflow : {}", checklistWorkflowDTO);
        if (checklistWorkflowDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklistWorkflow", "idexists", "A new checklistWorkflow cannot already have an ID")).body(null);
        }
        ChecklistWorkflowDTO result = checklistWorkflowService.save(checklistWorkflowDTO);
        return ResponseEntity.created(new URI("/api/checklist-workflows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklistWorkflow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklist-workflows : Updates an existing checklistWorkflow.
     *
     * @param checklistWorkflowDTO the checklistWorkflowDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklistWorkflowDTO,
     * or with status 400 (Bad Request) if the checklistWorkflowDTO is not valid,
     * or with status 500 (Internal Server Error) if the checklistWorkflowDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-workflows",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistWorkflowDTO> updateChecklistWorkflow(@RequestBody ChecklistWorkflowDTO checklistWorkflowDTO) throws URISyntaxException {
        log.debug("REST request to update ChecklistWorkflow : {}", checklistWorkflowDTO);
        if (checklistWorkflowDTO.getId() == null) {
            return createChecklistWorkflow(checklistWorkflowDTO);
        }
        ChecklistWorkflowDTO result = checklistWorkflowService.save(checklistWorkflowDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklistWorkflow", checklistWorkflowDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /checklist-workflows : get all the checklistWorkflows.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checklistWorkflows in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/checklist-workflows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistWorkflowDTO>> getAllChecklistWorkflows(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChecklistWorkflows");
        Page<ChecklistWorkflowDTO> page = checklistWorkflowService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/checklist-workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /checklist-workflows/:id : get the "id" checklistWorkflow.
     *
     * @param id the id of the checklistWorkflowDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklistWorkflowDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/checklist-workflows/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistWorkflowDTO> getChecklistWorkflow(@PathVariable Long id) {
        log.debug("REST request to get ChecklistWorkflow : {}", id);
        ChecklistWorkflowDTO checklistWorkflowDTO = checklistWorkflowService.findOne(id);
        return Optional.ofNullable(checklistWorkflowDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /checklist-workflows/:id : delete the "id" checklistWorkflow.
     *
     * @param id the id of the checklistWorkflowDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/checklist-workflows/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChecklistWorkflow(@PathVariable Long id) {
        log.debug("REST request to delete ChecklistWorkflow : {}", id);
        checklistWorkflowService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("checklistWorkflow", id.toString())).build();
    }

    /**
     * SEARCH  /_search/checklist-workflows?query=:query : search for the checklistWorkflow corresponding
     * to the query.
     *
     * @param query the query of the checklistWorkflow search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/checklist-workflows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistWorkflowDTO>> searchChecklistWorkflows(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChecklistWorkflows for query {}", query);
        Page<ChecklistWorkflowDTO> page = checklistWorkflowService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/checklist-workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
