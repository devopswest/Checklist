package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.Workflow;

import com.pwc.assurance.adc.repository.WorkflowRepository;
import com.pwc.assurance.adc.repository.search.WorkflowSearchRepository;
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
 * REST controller for managing Workflow.
 */
@RestController
@RequestMapping("/api")
public class WorkflowResource {

    private final Logger log = LoggerFactory.getLogger(WorkflowResource.class);
        
    @Inject
    private WorkflowRepository workflowRepository;

    @Inject
    private WorkflowSearchRepository workflowSearchRepository;

    /**
     * POST  /workflows : Create a new workflow.
     *
     * @param workflow the workflow to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workflow, or with status 400 (Bad Request) if the workflow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflows",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Workflow> createWorkflow(@RequestBody Workflow workflow) throws URISyntaxException {
        log.debug("REST request to save Workflow : {}", workflow);
        if (workflow.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workflow", "idexists", "A new workflow cannot already have an ID")).body(null);
        }
        Workflow result = workflowRepository.save(workflow);
        workflowSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/workflows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workflow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workflows : Updates an existing workflow.
     *
     * @param workflow the workflow to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workflow,
     * or with status 400 (Bad Request) if the workflow is not valid,
     * or with status 500 (Internal Server Error) if the workflow couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/workflows",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Workflow> updateWorkflow(@RequestBody Workflow workflow) throws URISyntaxException {
        log.debug("REST request to update Workflow : {}", workflow);
        if (workflow.getId() == null) {
            return createWorkflow(workflow);
        }
        Workflow result = workflowRepository.save(workflow);
        workflowSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workflow", workflow.getId().toString()))
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
    public ResponseEntity<List<Workflow>> getAllWorkflows(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Workflows");
        Page<Workflow> page = workflowRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workflows/:id : get the "id" workflow.
     *
     * @param id the id of the workflow to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workflow, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/workflows/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Workflow> getWorkflow(@PathVariable Long id) {
        log.debug("REST request to get Workflow : {}", id);
        Workflow workflow = workflowRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(workflow)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workflows/:id : delete the "id" workflow.
     *
     * @param id the id of the workflow to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/workflows/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id) {
        log.debug("REST request to delete Workflow : {}", id);
        workflowRepository.delete(id);
        workflowSearchRepository.delete(id);
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
    public ResponseEntity<List<Workflow>> searchWorkflows(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Workflows for query {}", query);
        Page<Workflow> page = workflowSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
