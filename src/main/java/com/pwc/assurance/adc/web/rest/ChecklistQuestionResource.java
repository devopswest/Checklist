package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.ChecklistQuestion;

import com.pwc.assurance.adc.repository.ChecklistQuestionRepository;
import com.pwc.assurance.adc.repository.search.ChecklistQuestionSearchRepository;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.ChecklistQuestionDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistQuestionMapper;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
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
    private ChecklistQuestionMapper checklistQuestionMapper;

    @Inject
    private ChecklistQuestionSearchRepository checklistQuestionSearchRepository;

    /**
     * POST  /checklist-questions : Create a new checklistQuestion.
     *
     * @param checklistQuestionDTO the checklistQuestionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklistQuestionDTO, or with status 400 (Bad Request) if the checklistQuestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-questions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistQuestionDTO> createChecklistQuestion(@Valid @RequestBody ChecklistQuestionDTO checklistQuestionDTO) throws URISyntaxException {
        log.debug("REST request to save ChecklistQuestion : {}", checklistQuestionDTO);
        if (checklistQuestionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklistQuestion", "idexists", "A new checklistQuestion cannot already have an ID")).body(null);
        }
        ChecklistQuestion checklistQuestion = checklistQuestionMapper.checklistQuestionDTOToChecklistQuestion(checklistQuestionDTO);
        checklistQuestion = checklistQuestionRepository.save(checklistQuestion);
        ChecklistQuestionDTO result = checklistQuestionMapper.checklistQuestionToChecklistQuestionDTO(checklistQuestion);
        checklistQuestionSearchRepository.save(checklistQuestion);
        return ResponseEntity.created(new URI("/api/checklist-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklistQuestion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklist-questions : Updates an existing checklistQuestion.
     *
     * @param checklistQuestionDTO the checklistQuestionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklistQuestionDTO,
     * or with status 400 (Bad Request) if the checklistQuestionDTO is not valid,
     * or with status 500 (Internal Server Error) if the checklistQuestionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklist-questions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistQuestionDTO> updateChecklistQuestion(@Valid @RequestBody ChecklistQuestionDTO checklistQuestionDTO) throws URISyntaxException {
        log.debug("REST request to update ChecklistQuestion : {}", checklistQuestionDTO);
        if (checklistQuestionDTO.getId() == null) {
            return createChecklistQuestion(checklistQuestionDTO);
        }
        ChecklistQuestion checklistQuestion = checklistQuestionMapper.checklistQuestionDTOToChecklistQuestion(checklistQuestionDTO);
        checklistQuestion = checklistQuestionRepository.save(checklistQuestion);
        ChecklistQuestionDTO result = checklistQuestionMapper.checklistQuestionToChecklistQuestionDTO(checklistQuestion);
        checklistQuestionSearchRepository.save(checklistQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklistQuestion", checklistQuestionDTO.getId().toString()))
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
    public ResponseEntity<List<ChecklistQuestionDTO>> getAllChecklistQuestions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ChecklistQuestions");
        Page<ChecklistQuestion> page = checklistQuestionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/checklist-questions");
        return new ResponseEntity<>(checklistQuestionMapper.checklistQuestionsToChecklistQuestionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /checklist-questions/:id : get the "id" checklistQuestion.
     *
     * @param id the id of the checklistQuestionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklistQuestionDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/checklist-questions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChecklistQuestionDTO> getChecklistQuestion(@PathVariable Long id) {
        log.debug("REST request to get ChecklistQuestion : {}", id);
        ChecklistQuestion checklistQuestion = checklistQuestionRepository.findOne(id);
        ChecklistQuestionDTO checklistQuestionDTO = checklistQuestionMapper.checklistQuestionToChecklistQuestionDTO(checklistQuestion);
        return Optional.ofNullable(checklistQuestionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /checklist-questions/:id : delete the "id" checklistQuestion.
     *
     * @param id the id of the checklistQuestionDTO to delete
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
    public ResponseEntity<List<ChecklistQuestionDTO>> searchChecklistQuestions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ChecklistQuestions for query {}", query);
        Page<ChecklistQuestion> page = checklistQuestionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/checklist-questions");
        return new ResponseEntity<>(checklistQuestionMapper.checklistQuestionsToChecklistQuestionDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
