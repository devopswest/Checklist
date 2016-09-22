package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.ChecklistAnswerService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.ChecklistAnswerDTO;
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
 * REST controller for managing ChecklistAnswer.
 */
@RestController
@RequestMapping("/api")
public class ChecklistAnswerResource {

    private final Logger log = LoggerFactory.getLogger(ChecklistAnswerResource.class);
        
    @Inject
    private ChecklistAnswerService checklistAnswerService;

    /**
     * POST  /checklist-answers : Create a new checklistAnswer.
     *
     * @param checklistAnswerDTO the checklistAnswerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklistAnswerDTO, or with status 400 (Bad Request) if the checklistAnswer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-answers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistAnswerDTO> createChecklistAnswer(@RequestBody ChecklistAnswerDTO checklistAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save ChecklistAnswer : {}", checklistAnswerDTO);
        if (checklistAnswerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklistAnswer", "idexists", "A new checklistAnswer cannot already have an ID")).body(null);
        }
        ChecklistAnswerDTO result = checklistAnswerService.save(checklistAnswerDTO);
        return ResponseEntity.created(new URI("/api/checklist-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklistAnswer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklist-answers : Updates an existing checklistAnswer.
     *
     * @param checklistAnswerDTO the checklistAnswerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklistAnswerDTO,
     * or with status 400 (Bad Request) if the checklistAnswerDTO is not valid,
     * or with status 500 (Internal Server Error) if the checklistAnswerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-answers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistAnswerDTO> updateChecklistAnswer(@RequestBody ChecklistAnswerDTO checklistAnswerDTO) throws URISyntaxException {
        log.debug("REST request to update ChecklistAnswer : {}", checklistAnswerDTO);
        if (checklistAnswerDTO.getId() == null) {
            return createChecklistAnswer(checklistAnswerDTO);
        }
        ChecklistAnswerDTO result = checklistAnswerService.save(checklistAnswerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklistAnswer", checklistAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /checklist-answers : get all the checklistAnswers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checklistAnswers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/checklist-answers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistAnswerDTO>> getAllChecklistAnswers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChecklistAnswers");
        Page<ChecklistAnswerDTO> page = checklistAnswerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/checklist-answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /checklist-answers/:id : get the "id" checklistAnswer.
     *
     * @param id the id of the checklistAnswerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklistAnswerDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/checklist-answers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistAnswerDTO> getChecklistAnswer(@PathVariable Long id) {
        log.debug("REST request to get ChecklistAnswer : {}", id);
        ChecklistAnswerDTO checklistAnswerDTO = checklistAnswerService.findOne(id);
        return Optional.ofNullable(checklistAnswerDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /checklist-answers/:id : delete the "id" checklistAnswer.
     *
     * @param id the id of the checklistAnswerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/checklist-answers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChecklistAnswer(@PathVariable Long id) {
        log.debug("REST request to delete ChecklistAnswer : {}", id);
        checklistAnswerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("checklistAnswer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/checklist-answers?query=:query : search for the checklistAnswer corresponding
     * to the query.
     *
     * @param query the query of the checklistAnswer search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/checklist-answers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistAnswerDTO>> searchChecklistAnswers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChecklistAnswers for query {}", query);
        Page<ChecklistAnswerDTO> page = checklistAnswerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/checklist-answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
