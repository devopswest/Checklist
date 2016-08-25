package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.ChecklistQuestion;

import com.pwc.assurance.adc.repository.ChecklistQuestionRepository;
import com.pwc.assurance.adc.repository.search.ChecklistQuestionSearchRepository;
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
 * REST controller for managing ChecklistQuestion.
 */
@RestController
@RequestMapping("/api")
public class ChecklistQuestionResource {

    private final Logger log = LoggerFactory.getLogger(ChecklistQuestionResource.class);
        
    @Inject
    private ChecklistQuestionRepository checklistQuestionRepository;

    @Inject
    private ChecklistQuestionSearchRepository checklistQuestionSearchRepository;

    /**
     * POST  /checklist-questions : Create a new checklistQuestion.
     *
     * @param checklistQuestion the checklistQuestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklistQuestion, or with status 400 (Bad Request) if the checklistQuestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-questions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistQuestion> createChecklistQuestion(@RequestBody ChecklistQuestion checklistQuestion) throws URISyntaxException {
        log.debug("REST request to save ChecklistQuestion : {}", checklistQuestion);
        if (checklistQuestion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklistQuestion", "idexists", "A new checklistQuestion cannot already have an ID")).body(null);
        }
        ChecklistQuestion result = checklistQuestionRepository.save(checklistQuestion);
        checklistQuestionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/checklist-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklistQuestion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklist-questions : Updates an existing checklistQuestion.
     *
     * @param checklistQuestion the checklistQuestion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklistQuestion,
     * or with status 400 (Bad Request) if the checklistQuestion is not valid,
     * or with status 500 (Internal Server Error) if the checklistQuestion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-questions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistQuestion> updateChecklistQuestion(@RequestBody ChecklistQuestion checklistQuestion) throws URISyntaxException {
        log.debug("REST request to update ChecklistQuestion : {}", checklistQuestion);
        if (checklistQuestion.getId() == null) {
            return createChecklistQuestion(checklistQuestion);
        }
        ChecklistQuestion result = checklistQuestionRepository.save(checklistQuestion);
        checklistQuestionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklistQuestion", checklistQuestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /checklist-questions : get all the checklistQuestions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checklistQuestions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/checklist-questions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistQuestion>> getAllChecklistQuestions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChecklistQuestions");
        Page<ChecklistQuestion> page = checklistQuestionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/checklist-questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /checklist-questions/:id : get the "id" checklistQuestion.
     *
     * @param id the id of the checklistQuestion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklistQuestion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/checklist-questions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistQuestion> getChecklistQuestion(@PathVariable Long id) {
        log.debug("REST request to get ChecklistQuestion : {}", id);
        ChecklistQuestion checklistQuestion = checklistQuestionRepository.findOne(id);
        return Optional.ofNullable(checklistQuestion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /checklist-questions/:id : delete the "id" checklistQuestion.
     *
     * @param id the id of the checklistQuestion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/checklist-questions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChecklistQuestion(@PathVariable Long id) {
        log.debug("REST request to delete ChecklistQuestion : {}", id);
        checklistQuestionRepository.delete(id);
        checklistQuestionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("checklistQuestion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/checklist-questions?query=:query : search for the checklistQuestion corresponding
     * to the query.
     *
     * @param query the query of the checklistQuestion search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/checklist-questions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChecklistQuestion>> searchChecklistQuestions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChecklistQuestions for query {}", query);
        Page<ChecklistQuestion> page = checklistQuestionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/checklist-questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
