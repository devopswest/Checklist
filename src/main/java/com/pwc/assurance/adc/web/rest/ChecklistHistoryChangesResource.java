package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.ChecklistHistoryChangesService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.ChecklistHistoryChangesDTO;
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
 * REST controller for managing ChecklistHistoryChanges.
 */
@RestController
@RequestMapping("/api")
public class ChecklistHistoryChangesResource {

    private final Logger log = LoggerFactory.getLogger(ChecklistHistoryChangesResource.class);
        
    @Inject
    private ChecklistHistoryChangesService checklistHistoryChangesService;

    /**
     * POST  /checklist-history-changes : Create a new checklistHistoryChanges.
     *
     * @param checklistHistoryChangesDTO the checklistHistoryChangesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklistHistoryChangesDTO, or with status 400 (Bad Request) if the checklistHistoryChanges has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-history-changes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistHistoryChangesDTO> createChecklistHistoryChanges(@RequestBody ChecklistHistoryChangesDTO checklistHistoryChangesDTO) throws URISyntaxException {
        log.debug("REST request to save ChecklistHistoryChanges : {}", checklistHistoryChangesDTO);
        if (checklistHistoryChangesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklistHistoryChanges", "idexists", "A new checklistHistoryChanges cannot already have an ID")).body(null);
        }
        ChecklistHistoryChangesDTO result = checklistHistoryChangesService.save(checklistHistoryChangesDTO);
        return ResponseEntity.created(new URI("/api/checklist-history-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklistHistoryChanges", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklist-history-changes : Updates an existing checklistHistoryChanges.
     *
     * @param checklistHistoryChangesDTO the checklistHistoryChangesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklistHistoryChangesDTO,
     * or with status 400 (Bad Request) if the checklistHistoryChangesDTO is not valid,
     * or with status 500 (Internal Server Error) if the checklistHistoryChangesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-history-changes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistHistoryChangesDTO> updateChecklistHistoryChanges(@RequestBody ChecklistHistoryChangesDTO checklistHistoryChangesDTO) throws URISyntaxException {
        log.debug("REST request to update ChecklistHistoryChanges : {}", checklistHistoryChangesDTO);
        if (checklistHistoryChangesDTO.getId() == null) {
            return createChecklistHistoryChanges(checklistHistoryChangesDTO);
        }
        ChecklistHistoryChangesDTO result = checklistHistoryChangesService.save(checklistHistoryChangesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklistHistoryChanges", checklistHistoryChangesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /checklist-history-changes : get all the checklistHistoryChanges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checklistHistoryChanges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/checklist-history-changes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistHistoryChangesDTO>> getAllChecklistHistoryChanges(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChecklistHistoryChanges");
        Page<ChecklistHistoryChangesDTO> page = checklistHistoryChangesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/checklist-history-changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /checklist-history-changes/:id : get the "id" checklistHistoryChanges.
     *
     * @param id the id of the checklistHistoryChangesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklistHistoryChangesDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/checklist-history-changes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistHistoryChangesDTO> getChecklistHistoryChanges(@PathVariable Long id) {
        log.debug("REST request to get ChecklistHistoryChanges : {}", id);
        ChecklistHistoryChangesDTO checklistHistoryChangesDTO = checklistHistoryChangesService.findOne(id);
        return Optional.ofNullable(checklistHistoryChangesDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /checklist-history-changes/:id : delete the "id" checklistHistoryChanges.
     *
     * @param id the id of the checklistHistoryChangesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/checklist-history-changes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChecklistHistoryChanges(@PathVariable Long id) {
        log.debug("REST request to delete ChecklistHistoryChanges : {}", id);
        checklistHistoryChangesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("checklistHistoryChanges", id.toString())).build();
    }

    /**
     * SEARCH  /_search/checklist-history-changes?query=:query : search for the checklistHistoryChanges corresponding
     * to the query.
     *
     * @param query the query of the checklistHistoryChanges search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/checklist-history-changes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistHistoryChangesDTO>> searchChecklistHistoryChanges(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChecklistHistoryChanges for query {}", query);
        Page<ChecklistHistoryChangesDTO> page = checklistHistoryChangesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/checklist-history-changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
