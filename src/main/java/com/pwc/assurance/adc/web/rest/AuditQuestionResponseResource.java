package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.AuditQuestionResponse;

import com.pwc.assurance.adc.repository.AuditQuestionResponseRepository;
import com.pwc.assurance.adc.repository.search.AuditQuestionResponseSearchRepository;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.AuditQuestionResponseDTO;
import com.pwc.assurance.adc.service.mapper.AuditQuestionResponseMapper;
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
 * REST controller for managing AuditQuestionResponse.
 */
@RestController
@RequestMapping("/api")
public class AuditQuestionResponseResource {

    private final Logger log = LoggerFactory.getLogger(AuditQuestionResponseResource.class);
        
    @Inject
    private AuditQuestionResponseRepository auditQuestionResponseRepository;

    @Inject
    private AuditQuestionResponseMapper auditQuestionResponseMapper;

    @Inject
    private AuditQuestionResponseSearchRepository auditQuestionResponseSearchRepository;

    /**
     * POST  /audit-question-responses : Create a new auditQuestionResponse.
     *
     * @param auditQuestionResponseDTO the auditQuestionResponseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auditQuestionResponseDTO, or with status 400 (Bad Request) if the auditQuestionResponse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audit-question-responses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditQuestionResponseDTO> createAuditQuestionResponse(@RequestBody AuditQuestionResponseDTO auditQuestionResponseDTO) throws URISyntaxException {
        log.debug("REST request to save AuditQuestionResponse : {}", auditQuestionResponseDTO);
        if (auditQuestionResponseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auditQuestionResponse", "idexists", "A new auditQuestionResponse cannot already have an ID")).body(null);
        }
        AuditQuestionResponse auditQuestionResponse = auditQuestionResponseMapper.auditQuestionResponseDTOToAuditQuestionResponse(auditQuestionResponseDTO);
        auditQuestionResponse = auditQuestionResponseRepository.save(auditQuestionResponse);
        AuditQuestionResponseDTO result = auditQuestionResponseMapper.auditQuestionResponseToAuditQuestionResponseDTO(auditQuestionResponse);
        auditQuestionResponseSearchRepository.save(auditQuestionResponse);
        return ResponseEntity.created(new URI("/api/audit-question-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("auditQuestionResponse", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audit-question-responses : Updates an existing auditQuestionResponse.
     *
     * @param auditQuestionResponseDTO the auditQuestionResponseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auditQuestionResponseDTO,
     * or with status 400 (Bad Request) if the auditQuestionResponseDTO is not valid,
     * or with status 500 (Internal Server Error) if the auditQuestionResponseDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audit-question-responses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditQuestionResponseDTO> updateAuditQuestionResponse(@RequestBody AuditQuestionResponseDTO auditQuestionResponseDTO) throws URISyntaxException {
        log.debug("REST request to update AuditQuestionResponse : {}", auditQuestionResponseDTO);
        if (auditQuestionResponseDTO.getId() == null) {
            return createAuditQuestionResponse(auditQuestionResponseDTO);
        }
        AuditQuestionResponse auditQuestionResponse = auditQuestionResponseMapper.auditQuestionResponseDTOToAuditQuestionResponse(auditQuestionResponseDTO);
        auditQuestionResponse = auditQuestionResponseRepository.save(auditQuestionResponse);
        AuditQuestionResponseDTO result = auditQuestionResponseMapper.auditQuestionResponseToAuditQuestionResponseDTO(auditQuestionResponse);
        auditQuestionResponseSearchRepository.save(auditQuestionResponse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("auditQuestionResponse", auditQuestionResponseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audit-question-responses : get all the auditQuestionResponses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of auditQuestionResponses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/audit-question-responses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuditQuestionResponseDTO>> getAllAuditQuestionResponses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AuditQuestionResponses");
        Page<AuditQuestionResponse> page = auditQuestionResponseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/audit-question-responses");
        return new ResponseEntity<>(auditQuestionResponseMapper.auditQuestionResponsesToAuditQuestionResponseDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /audit-question-responses/:id : get the "id" auditQuestionResponse.
     *
     * @param id the id of the auditQuestionResponseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auditQuestionResponseDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/audit-question-responses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditQuestionResponseDTO> getAuditQuestionResponse(@PathVariable Long id) {
        log.debug("REST request to get AuditQuestionResponse : {}", id);
        AuditQuestionResponse auditQuestionResponse = auditQuestionResponseRepository.findOne(id);
        AuditQuestionResponseDTO auditQuestionResponseDTO = auditQuestionResponseMapper.auditQuestionResponseToAuditQuestionResponseDTO(auditQuestionResponse);
        return Optional.ofNullable(auditQuestionResponseDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /audit-question-responses/:id : delete the "id" auditQuestionResponse.
     *
     * @param id the id of the auditQuestionResponseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/audit-question-responses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAuditQuestionResponse(@PathVariable Long id) {
        log.debug("REST request to delete AuditQuestionResponse : {}", id);
        auditQuestionResponseRepository.delete(id);
        auditQuestionResponseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("auditQuestionResponse", id.toString())).build();
    }

    /**
     * SEARCH  /_search/audit-question-responses?query=:query : search for the auditQuestionResponse corresponding
     * to the query.
     *
     * @param query the query of the auditQuestionResponse search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/audit-question-responses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuditQuestionResponseDTO>> searchAuditQuestionResponses(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AuditQuestionResponses for query {}", query);
        Page<AuditQuestionResponse> page = auditQuestionResponseSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/audit-question-responses");
        return new ResponseEntity<>(auditQuestionResponseMapper.auditQuestionResponsesToAuditQuestionResponseDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
