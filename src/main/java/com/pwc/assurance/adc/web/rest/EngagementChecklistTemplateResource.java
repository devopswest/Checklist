package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.EngagementChecklistTemplateService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.EngagementChecklistTemplateDTO;
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
 * REST controller for managing EngagementChecklistTemplate.
 */
@RestController
@RequestMapping("/api")
public class EngagementChecklistTemplateResource {

    private final Logger log = LoggerFactory.getLogger(EngagementChecklistTemplateResource.class);
        
    @Inject
    private EngagementChecklistTemplateService engagementChecklistTemplateService;

    /**
     * POST  /engagement-checklist-templates : Create a new engagementChecklistTemplate.
     *
     * @param engagementChecklistTemplateDTO the engagementChecklistTemplateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new engagementChecklistTemplateDTO, or with status 400 (Bad Request) if the engagementChecklistTemplate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagement-checklist-templates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementChecklistTemplateDTO> createEngagementChecklistTemplate(@RequestBody EngagementChecklistTemplateDTO engagementChecklistTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save EngagementChecklistTemplate : {}", engagementChecklistTemplateDTO);
        if (engagementChecklistTemplateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("engagementChecklistTemplate", "idexists", "A new engagementChecklistTemplate cannot already have an ID")).body(null);
        }
        EngagementChecklistTemplateDTO result = engagementChecklistTemplateService.save(engagementChecklistTemplateDTO);
        return ResponseEntity.created(new URI("/api/engagement-checklist-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("engagementChecklistTemplate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /engagement-checklist-templates : Updates an existing engagementChecklistTemplate.
     *
     * @param engagementChecklistTemplateDTO the engagementChecklistTemplateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated engagementChecklistTemplateDTO,
     * or with status 400 (Bad Request) if the engagementChecklistTemplateDTO is not valid,
     * or with status 500 (Internal Server Error) if the engagementChecklistTemplateDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/engagement-checklist-templates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementChecklistTemplateDTO> updateEngagementChecklistTemplate(@RequestBody EngagementChecklistTemplateDTO engagementChecklistTemplateDTO) throws URISyntaxException {
        log.debug("REST request to update EngagementChecklistTemplate : {}", engagementChecklistTemplateDTO);
        if (engagementChecklistTemplateDTO.getId() == null) {
            return createEngagementChecklistTemplate(engagementChecklistTemplateDTO);
        }
        EngagementChecklistTemplateDTO result = engagementChecklistTemplateService.save(engagementChecklistTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("engagementChecklistTemplate", engagementChecklistTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /engagement-checklist-templates : get all the engagementChecklistTemplates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of engagementChecklistTemplates in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/engagement-checklist-templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EngagementChecklistTemplateDTO>> getAllEngagementChecklistTemplates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EngagementChecklistTemplates");
        Page<EngagementChecklistTemplateDTO> page = engagementChecklistTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/engagement-checklist-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /engagement-checklist-templates/:id : get the "id" engagementChecklistTemplate.
     *
     * @param id the id of the engagementChecklistTemplateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the engagementChecklistTemplateDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/engagement-checklist-templates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EngagementChecklistTemplateDTO> getEngagementChecklistTemplate(@PathVariable Long id) {
        log.debug("REST request to get EngagementChecklistTemplate : {}", id);
        EngagementChecklistTemplateDTO engagementChecklistTemplateDTO = engagementChecklistTemplateService.findOne(id);
        return Optional.ofNullable(engagementChecklistTemplateDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /engagement-checklist-templates/:id : delete the "id" engagementChecklistTemplate.
     *
     * @param id the id of the engagementChecklistTemplateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/engagement-checklist-templates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEngagementChecklistTemplate(@PathVariable Long id) {
        log.debug("REST request to delete EngagementChecklistTemplate : {}", id);
        engagementChecklistTemplateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("engagementChecklistTemplate", id.toString())).build();
    }

    /**
     * SEARCH  /_search/engagement-checklist-templates?query=:query : search for the engagementChecklistTemplate corresponding
     * to the query.
     *
     * @param query the query of the engagementChecklistTemplate search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/engagement-checklist-templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EngagementChecklistTemplateDTO>> searchEngagementChecklistTemplates(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of EngagementChecklistTemplates for query {}", query);
        Page<EngagementChecklistTemplateDTO> page = engagementChecklistTemplateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/engagement-checklist-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
