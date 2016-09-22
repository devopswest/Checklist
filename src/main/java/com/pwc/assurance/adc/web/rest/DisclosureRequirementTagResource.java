package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.DisclosureRequirementTagService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementTagDTO;
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
 * REST controller for managing DisclosureRequirementTag.
 */
@RestController
@RequestMapping("/api")
public class DisclosureRequirementTagResource {

    private final Logger log = LoggerFactory.getLogger(DisclosureRequirementTagResource.class);
        
    @Inject
    private DisclosureRequirementTagService disclosureRequirementTagService;

    /**
     * POST  /disclosure-requirement-tags : Create a new disclosureRequirementTag.
     *
     * @param disclosureRequirementTagDTO the disclosureRequirementTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new disclosureRequirementTagDTO, or with status 400 (Bad Request) if the disclosureRequirementTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/disclosure-requirement-tags",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisclosureRequirementTagDTO> createDisclosureRequirementTag(@RequestBody DisclosureRequirementTagDTO disclosureRequirementTagDTO) throws URISyntaxException {
        log.debug("REST request to save DisclosureRequirementTag : {}", disclosureRequirementTagDTO);
        if (disclosureRequirementTagDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("disclosureRequirementTag", "idexists", "A new disclosureRequirementTag cannot already have an ID")).body(null);
        }
        DisclosureRequirementTagDTO result = disclosureRequirementTagService.save(disclosureRequirementTagDTO);
        return ResponseEntity.created(new URI("/api/disclosure-requirement-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("disclosureRequirementTag", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disclosure-requirement-tags : Updates an existing disclosureRequirementTag.
     *
     * @param disclosureRequirementTagDTO the disclosureRequirementTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated disclosureRequirementTagDTO,
     * or with status 400 (Bad Request) if the disclosureRequirementTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the disclosureRequirementTagDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/disclosure-requirement-tags",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisclosureRequirementTagDTO> updateDisclosureRequirementTag(@RequestBody DisclosureRequirementTagDTO disclosureRequirementTagDTO) throws URISyntaxException {
        log.debug("REST request to update DisclosureRequirementTag : {}", disclosureRequirementTagDTO);
        if (disclosureRequirementTagDTO.getId() == null) {
            return createDisclosureRequirementTag(disclosureRequirementTagDTO);
        }
        DisclosureRequirementTagDTO result = disclosureRequirementTagService.save(disclosureRequirementTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("disclosureRequirementTag", disclosureRequirementTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disclosure-requirement-tags : get all the disclosureRequirementTags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of disclosureRequirementTags in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/disclosure-requirement-tags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DisclosureRequirementTagDTO>> getAllDisclosureRequirementTags(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DisclosureRequirementTags");
        Page<DisclosureRequirementTagDTO> page = disclosureRequirementTagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/disclosure-requirement-tags");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /disclosure-requirement-tags/:id : get the "id" disclosureRequirementTag.
     *
     * @param id the id of the disclosureRequirementTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the disclosureRequirementTagDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/disclosure-requirement-tags/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisclosureRequirementTagDTO> getDisclosureRequirementTag(@PathVariable Long id) {
        log.debug("REST request to get DisclosureRequirementTag : {}", id);
        DisclosureRequirementTagDTO disclosureRequirementTagDTO = disclosureRequirementTagService.findOne(id);
        return Optional.ofNullable(disclosureRequirementTagDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /disclosure-requirement-tags/:id : delete the "id" disclosureRequirementTag.
     *
     * @param id the id of the disclosureRequirementTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/disclosure-requirement-tags/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDisclosureRequirementTag(@PathVariable Long id) {
        log.debug("REST request to delete DisclosureRequirementTag : {}", id);
        disclosureRequirementTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("disclosureRequirementTag", id.toString())).build();
    }

    /**
     * SEARCH  /_search/disclosure-requirement-tags?query=:query : search for the disclosureRequirementTag corresponding
     * to the query.
     *
     * @param query the query of the disclosureRequirementTag search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/disclosure-requirement-tags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DisclosureRequirementTagDTO>> searchDisclosureRequirementTags(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DisclosureRequirementTags for query {}", query);
        Page<DisclosureRequirementTagDTO> page = disclosureRequirementTagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/disclosure-requirement-tags");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
