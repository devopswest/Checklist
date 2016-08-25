package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.Checklist;

import com.pwc.assurance.adc.repository.ChecklistRepository;
import com.pwc.assurance.adc.repository.search.ChecklistSearchRepository;
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
 * REST controller for managing Checklist.
 */
@RestController
@RequestMapping("/api")
public class ChecklistResource {

    private final Logger log = LoggerFactory.getLogger(ChecklistResource.class);
        
    @Inject
    private ChecklistRepository checklistRepository;

    @Inject
    private ChecklistSearchRepository checklistSearchRepository;

    /**
     * POST  /checklists : Create a new checklist.
     *
     * @param checklist the checklist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklist, or with status 400 (Bad Request) if the checklist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Checklist> createChecklist(@RequestBody Checklist checklist) throws URISyntaxException {
        log.debug("REST request to save Checklist : {}", checklist);
        if (checklist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklist", "idexists", "A new checklist cannot already have an ID")).body(null);
        }
        Checklist result = checklistRepository.save(checklist);
        checklistSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/checklists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklists : Updates an existing checklist.
     *
     * @param checklist the checklist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklist,
     * or with status 400 (Bad Request) if the checklist is not valid,
     * or with status 500 (Internal Server Error) if the checklist couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Checklist> updateChecklist(@RequestBody Checklist checklist) throws URISyntaxException {
        log.debug("REST request to update Checklist : {}", checklist);
        if (checklist.getId() == null) {
            return createChecklist(checklist);
        }
        Checklist result = checklistRepository.save(checklist);
        checklistSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklist", checklist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /checklists : get all the checklists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checklists in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/checklists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Checklist>> getAllChecklists(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Checklists");
        Page<Checklist> page = checklistRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/checklists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /checklists/:id : get the "id" checklist.
     *
     * @param id the id of the checklist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklist, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/checklists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Checklist> getChecklist(@PathVariable Long id) {
        log.debug("REST request to get Checklist : {}", id);
        Checklist checklist = checklistRepository.findOne(id);
        return Optional.ofNullable(checklist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /checklists/:id : delete the "id" checklist.
     *
     * @param id the id of the checklist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/checklists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChecklist(@PathVariable Long id) {
        log.debug("REST request to delete Checklist : {}", id);
        checklistRepository.delete(id);
        checklistSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("checklist", id.toString())).build();
    }

    /**
     * SEARCH  /_search/checklists?query=:query : search for the checklist corresponding
     * to the query.
     *
     * @param query the query of the checklist search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/checklists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Checklist>> searchChecklists(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Checklists for query {}", query);
        Page<Checklist> page = checklistSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/checklists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
