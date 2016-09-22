package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.WorkflowService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.WorkflowDTO;
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
 * REST controller for managing Workflow.
 */
@RestController
@RequestMapping("/api")
public class WorkflowResource {

    private final Logger log = LoggerFactory.getLogger(WorkflowResource.class);
        
    @Inject
    private WorkflowService workflowService;

    /**
     * POST  /workflows : Create a new workflow.
     *
     * @param workflowDTO the workflowDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workflowDTO, or with status 400 (Bad Request) if the workflow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflows",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowDTO> createWorkflow(@RequestBody WorkflowDTO workflowDTO) throws URISyntaxException {
        log.debug("REST request to save Workflow : {}", workflowDTO);
        if (workflowDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workflow", "idexists", "A new workflow cannot already have an ID")).body(null);
        }
        WorkflowDTO result = workflowService.save(workflowDTO);
        return ResponseEntity.created(new URI("/api/workflows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workflow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workflows : Updates an existing workflow.
     *
     * @param workflowDTO the workflowDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workflowDTO,
     * or with status 400 (Bad Request) if the workflowDTO is not valid,
     * or with status 500 (Internal Server Error) if the workflowDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflows",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowDTO> updateWorkflow(@RequestBody WorkflowDTO workflowDTO) throws URISyntaxException {
        log.debug("REST request to update Workflow : {}", workflowDTO);
        if (workflowDTO.getId() == null) {
            return createWorkflow(workflowDTO);
        }
        WorkflowDTO result = workflowService.save(workflowDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workflow", workflowDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workflows : get all the workflows.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workflows in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/workflows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkflowDTO>> getAllWorkflows(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Workflows");
        Page<WorkflowDTO> page = workflowService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workflows/:id : get the "id" workflow.
     *
     * @param id the id of the workflowDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workflowDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/workflows/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowDTO> getWorkflow(@PathVariable Long id) {
        log.debug("REST request to get Workflow : {}", id);
        WorkflowDTO workflowDTO = workflowService.findOne(id);
        return Optional.ofNullable(workflowDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workflows/:id : delete the "id" workflow.
     *
     * @param id the id of the workflowDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/workflows/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id) {
        log.debug("REST request to delete Workflow : {}", id);
        workflowService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workflow", id.toString())).build();
    }

    /**
     * SEARCH  /_search/workflows?query=:query : search for the workflow corresponding
     * to the query.
     *
     * @param query the query of the workflow search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/workflows",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkflowDTO>> searchWorkflows(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Workflows for query {}", query);
        Page<WorkflowDTO> page = workflowService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
