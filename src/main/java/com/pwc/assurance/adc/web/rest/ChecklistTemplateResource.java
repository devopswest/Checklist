package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.ChecklistTemplateService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.ChecklistTemplateDTO;
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
 * REST controller for managing ChecklistTemplate.
 */
@RestController
@RequestMapping("/api")
public class ChecklistTemplateResource {

    private final Logger log = LoggerFactory.getLogger(ChecklistTemplateResource.class);
        
    @Inject
    private ChecklistTemplateService checklistTemplateService;

    /**
     * POST  /checklist-templates : Create a new checklistTemplate.
     *
     * @param checklistTemplateDTO the checklistTemplateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklistTemplateDTO, or with status 400 (Bad Request) if the checklistTemplate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-templates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistTemplateDTO> createChecklistTemplate(@RequestBody ChecklistTemplateDTO checklistTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save ChecklistTemplate : {}", checklistTemplateDTO);
        if (checklistTemplateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklistTemplate", "idexists", "A new checklistTemplate cannot already have an ID")).body(null);
        }
        ChecklistTemplateDTO result = checklistTemplateService.save(checklistTemplateDTO);
        return ResponseEntity.created(new URI("/api/checklist-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklistTemplate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklist-templates : Updates an existing checklistTemplate.
     *
     * @param checklistTemplateDTO the checklistTemplateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklistTemplateDTO,
     * or with status 400 (Bad Request) if the checklistTemplateDTO is not valid,
     * or with status 500 (Internal Server Error) if the checklistTemplateDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-templates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistTemplateDTO> updateChecklistTemplate(@RequestBody ChecklistTemplateDTO checklistTemplateDTO) throws URISyntaxException {
        log.debug("REST request to update ChecklistTemplate : {}", checklistTemplateDTO);
        if (checklistTemplateDTO.getId() == null) {
            return createChecklistTemplate(checklistTemplateDTO);
        }
        ChecklistTemplateDTO result = checklistTemplateService.save(checklistTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklistTemplate", checklistTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /checklist-templates : get all the checklistTemplates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checklistTemplates in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/checklist-templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistTemplateDTO>> getAllChecklistTemplates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChecklistTemplates");
        Page<ChecklistTemplateDTO> page = checklistTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/checklist-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /checklist-templates/:id : get the "id" checklistTemplate.
     *
     * @param id the id of the checklistTemplateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklistTemplateDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/checklist-templates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistTemplateDTO> getChecklistTemplate(@PathVariable Long id) {
        log.debug("REST request to get ChecklistTemplate : {}", id);
        ChecklistTemplateDTO checklistTemplateDTO = checklistTemplateService.findOne(id);
        return Optional.ofNullable(checklistTemplateDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /checklist-templates/:id : delete the "id" checklistTemplate.
     *
     * @param id the id of the checklistTemplateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/checklist-templates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChecklistTemplate(@PathVariable Long id) {
        log.debug("REST request to delete ChecklistTemplate : {}", id);
        checklistTemplateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("checklistTemplate", id.toString())).build();
    }

    /**
     * SEARCH  /_search/checklist-templates?query=:query : search for the checklistTemplate corresponding
     * to the query.
     *
     * @param query the query of the checklistTemplate search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/checklist-templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistTemplateDTO>> searchChecklistTemplates(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChecklistTemplates for query {}", query);
        Page<ChecklistTemplateDTO> page = checklistTemplateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/checklist-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
