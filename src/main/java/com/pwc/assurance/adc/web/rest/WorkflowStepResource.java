package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.WorkflowStep;

import com.pwc.assurance.adc.repository.WorkflowStepRepository;
import com.pwc.assurance.adc.repository.search.WorkflowStepSearchRepository;
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
 * REST controller for managing WorkflowStep.
 */
@RestController
@RequestMapping("/api")
public class WorkflowStepResource {

    private final Logger log = LoggerFactory.getLogger(WorkflowStepResource.class);
        
    @Inject
    private WorkflowStepRepository workflowStepRepository;

    @Inject
    private WorkflowStepSearchRepository workflowStepSearchRepository;

    /**
     * POST  /workflow-steps : Create a new workflowStep.
     *
     * @param workflowStep the workflowStep to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workflowStep, or with status 400 (Bad Request) if the workflowStep has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflow-steps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowStep> createWorkflowStep(@RequestBody WorkflowStep workflowStep) throws URISyntaxException {
        log.debug("REST request to save WorkflowStep : {}", workflowStep);
        if (workflowStep.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workflowStep", "idexists", "A new workflowStep cannot already have an ID")).body(null);
        }
        WorkflowStep result = workflowStepRepository.save(workflowStep);
        workflowStepSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/workflow-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workflowStep", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workflow-steps : Updates an existing workflowStep.
     *
     * @param workflowStep the workflowStep to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workflowStep,
     * or with status 400 (Bad Request) if the workflowStep is not valid,
     * or with status 500 (Internal Server Error) if the workflowStep couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflow-steps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowStep> updateWorkflowStep(@RequestBody WorkflowStep workflowStep) throws URISyntaxException {
        log.debug("REST request to update WorkflowStep : {}", workflowStep);
        if (workflowStep.getId() == null) {
            return createWorkflowStep(workflowStep);
        }
        WorkflowStep result = workflowStepRepository.save(workflowStep);
        workflowStepSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workflowStep", workflowStep.getId().toString()))
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
    public ResponseEntity<List<WorkflowStep>> getAllWorkflowSteps(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WorkflowSteps");
        Page<WorkflowStep> page = workflowStepRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workflow-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workflow-steps/:id : get the "id" workflowStep.
     *
     * @param id the id of the workflowStep to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workflowStep, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/workflow-steps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkflowStep> getWorkflowStep(@PathVariable Long id) {
        log.debug("REST request to get WorkflowStep : {}", id);
        WorkflowStep workflowStep = workflowStepRepository.findOne(id);
        return Optional.ofNullable(workflowStep)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workflow-steps/:id : delete the "id" workflowStep.
     *
     * @param id the id of the workflowStep to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/workflow-steps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkflowStep(@PathVariable Long id) {
        log.debug("REST request to delete WorkflowStep : {}", id);
        workflowStepRepository.delete(id);
        workflowStepSearchRepository.delete(id);
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
    public ResponseEntity<List<WorkflowStep>> searchWorkflowSteps(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WorkflowSteps for query {}", query);
        Page<WorkflowStep> page = workflowStepSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/workflow-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
