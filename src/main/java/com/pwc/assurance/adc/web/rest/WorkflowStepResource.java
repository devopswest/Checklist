package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.WorkflowStepService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.WorkflowStepDTO;
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
 * REST controller for managing WorkflowStep.
 */
@RestController
@RequestMapping("/api")
public class WorkflowStepResource {

    private final Logger log = LoggerFactory.getLogger(WorkflowStepResource.class);
        
    @Inject
    private WorkflowStepService workflowStepService;

    /**
     * POST  /workflow-steps : Create a new workflowStep.
     *
     * @param workflowStepDTO the workflowStepDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workflowStepDTO, or with status 400 (Bad Request) if the workflowStep has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflow-steps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowStepDTO> createWorkflowStep(@RequestBody WorkflowStepDTO workflowStepDTO) throws URISyntaxException {
        log.debug("REST request to save WorkflowStep : {}", workflowStepDTO);
        if (workflowStepDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workflowStep", "idexists", "A new workflowStep cannot already have an ID")).body(null);
        }
        WorkflowStepDTO result = workflowStepService.save(workflowStepDTO);
        return ResponseEntity.created(new URI("/api/workflow-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workflowStep", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workflow-steps : Updates an existing workflowStep.
     *
     * @param workflowStepDTO the workflowStepDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workflowStepDTO,
     * or with status 400 (Bad Request) if the workflowStepDTO is not valid,
     * or with status 500 (Internal Server Error) if the workflowStepDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflow-steps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowStepDTO> updateWorkflowStep(@RequestBody WorkflowStepDTO workflowStepDTO) throws URISyntaxException {
        log.debug("REST request to update WorkflowStep : {}", workflowStepDTO);
        if (workflowStepDTO.getId() == null) {
            return createWorkflowStep(workflowStepDTO);
        }
        WorkflowStepDTO result = workflowStepService.save(workflowStepDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workflowStep", workflowStepDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workflow-steps : get all the workflowSteps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workflowSteps in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/workflow-steps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkflowStepDTO>> getAllWorkflowSteps(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WorkflowSteps");
        Page<WorkflowStepDTO> page = workflowStepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workflow-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workflow-steps/:id : get the "id" workflowStep.
     *
     * @param id the id of the workflowStepDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workflowStepDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/workflow-steps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowStepDTO> getWorkflowStep(@PathVariable Long id) {
        log.debug("REST request to get WorkflowStep : {}", id);
        WorkflowStepDTO workflowStepDTO = workflowStepService.findOne(id);
        return Optional.ofNullable(workflowStepDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workflow-steps/:id : delete the "id" workflowStep.
     *
     * @param id the id of the workflowStepDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/workflow-steps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkflowStep(@PathVariable Long id) {
        log.debug("REST request to delete WorkflowStep : {}", id);
        workflowStepService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workflowStep", id.toString())).build();
    }

    /**
     * SEARCH  /_search/workflow-steps?query=:query : search for the workflowStep corresponding
     * to the query.
     *
     * @param query the query of the workflowStep search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/workflow-steps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkflowStepDTO>> searchWorkflowSteps(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WorkflowSteps for query {}", query);
        Page<WorkflowStepDTO> page = workflowStepService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/workflow-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
