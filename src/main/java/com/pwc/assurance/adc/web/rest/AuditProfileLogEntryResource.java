package com.pwc.assurance.adc.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.AuditProfileLogEntry;
import com.pwc.assurance.adc.domain.User;
import com.pwc.assurance.adc.repository.AuditProfileLogEntryRepository;
import com.pwc.assurance.adc.repository.UserRepository;
import com.pwc.assurance.adc.repository.search.AuditProfileLogEntrySearchRepository;
import com.pwc.assurance.adc.service.dto.AuditProfileLogEntryDTO;
import com.pwc.assurance.adc.service.mapper.AuditProfileLogEntryMapper;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;

/**
 * REST controller for managing AuditProfileLogEntry.
 */
@RestController
@RequestMapping("/api")
public class AuditProfileLogEntryResource {

    private final Logger log = LoggerFactory.getLogger(AuditProfileLogEntryResource.class);
        
    @Inject
    private AuditProfileLogEntryRepository auditProfileLogEntryRepository;

    @Inject
    private AuditProfileLogEntrySearchRepository auditProfileLogEntrySearchRepository;
    
    @Inject
    private AuditProfileLogEntryMapper auditProfileLogEntryMapper;
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /audit-profile-log-entries : Create a new auditProfileLogEntry.
     *
     * @param auditProfileLogEntry the auditProfileLogEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auditProfileLogEntry, or with status 400 (Bad Request) if the auditProfileLogEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audit-profile-log-entries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditProfileLogEntry> createAuditProfileLogEntry(@RequestBody AuditProfileLogEntry auditProfileLogEntry) throws URISyntaxException {
        log.debug("REST request to save AuditProfileLogEntry : {}", auditProfileLogEntry);
        if (auditProfileLogEntry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auditProfileLogEntry", "idexists", "A new auditProfileLogEntry cannot already have an ID")).body(null);
        }
        AuditProfileLogEntry result = auditProfileLogEntryRepository.save(auditProfileLogEntry);
        auditProfileLogEntrySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/audit-profile-log-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("auditProfileLogEntry", result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /audit-profile-log-entries/array : Create a multiple auditProfileLogEntry.
     *
     * @param auditProfileLogEntryDTOs List of auditProfileLogEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auditProfileLogEntry, or with status 400 (Bad Request) if the auditProfileLogEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audit-profile-log-entries/array",  method = RequestMethod.POST)
    @Timed
    public void createAuditProfileLogEntryArray(@RequestBody AuditProfileLogEntryDTOList auditProfileLogEntryDTOs) throws URISyntaxException {

        log.debug("REST request to save AuditProfileLogEntry : {}", auditProfileLogEntryDTOs);
        List<AuditProfileLogEntry> auditProfileLogEntryList = auditProfileLogEntryMapper.auditProfileLongEntryDTOToAuditProfileLogEntrys(auditProfileLogEntryDTOs);
        ZonedDateTime now = ZonedDateTime.now();
        User user = null;
        //Finding the user Id based on his login details
        for(AuditProfileLogEntryDTO dto: auditProfileLogEntryDTOs){
        	Long userId = getUserId(dto.getUserLogin());
        	if(userId != null){
        		user = new User();
        		user.setId(userId);
        	}
        	break;
        }
        for(AuditProfileLogEntry logEntry: auditProfileLogEntryList){
        	logEntry.setHappened(now);
        	logEntry.setWho(user);
        }
        List<AuditProfileLogEntry> result = auditProfileLogEntryRepository.save(auditProfileLogEntryList);
    	auditProfileLogEntrySearchRepository.save(result);
    	
    }
    
    /**
     * Look up login user based on Id
     * @param login
     * @return
     */
    private Long getUserId(String login){
    	 Optional<User> userObj = userRepository.findOneByLogin(login);
         if(userObj != null && userObj.isPresent()){
         	return userObj.get().getId();
         }
         return null;
    }
   
    static class AuditProfileLogEntryDTOList extends ArrayList<AuditProfileLogEntryDTO> {
		private static final long serialVersionUID = -6169807428647014244L; 
    };

    /**
     * PUT  /audit-profile-log-entries : Updates an existing auditProfileLogEntry.
     *
     * @param auditProfileLogEntry the auditProfileLogEntry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auditProfileLogEntry,
     * or with status 400 (Bad Request) if the auditProfileLogEntry is not valid,
     * or with status 500 (Internal Server Error) if the auditProfileLogEntry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/audit-profile-log-entries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditProfileLogEntry> updateAuditProfileLogEntry(@RequestBody AuditProfileLogEntry auditProfileLogEntry) throws URISyntaxException {
        log.debug("REST request to update AuditProfileLogEntry : {}", auditProfileLogEntry);
        if (auditProfileLogEntry.getId() == null) {
            return createAuditProfileLogEntry(auditProfileLogEntry);
        }
        AuditProfileLogEntry result = auditProfileLogEntryRepository.save(auditProfileLogEntry);
        auditProfileLogEntrySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("auditProfileLogEntry", auditProfileLogEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audit-profile-log-entries : get all the auditProfileLogEntries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of auditProfileLogEntries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/audit-profile-log-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuditProfileLogEntry>> getAllAuditProfileLogEntries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AuditProfileLogEntries");
        Page<AuditProfileLogEntry> page = auditProfileLogEntryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/audit-profile-log-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /audit-profile-log-entries/:id : get the "id" auditProfileLogEntry.
     *
     * @param id the id of the auditProfileLogEntry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auditProfileLogEntry, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/audit-profile-log-entries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditProfileLogEntry> getAuditProfileLogEntry(@PathVariable Long id) {
        log.debug("REST request to get AuditProfileLogEntry : {}", id);
        AuditProfileLogEntry auditProfileLogEntry = auditProfileLogEntryRepository.findOne(id);
        return Optional.ofNullable(auditProfileLogEntry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /audit-profile-log-entries/:id : delete the "id" auditProfileLogEntry.
     *
     * @param id the id of the auditProfileLogEntry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/audit-profile-log-entries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAuditProfileLogEntry(@PathVariable Long id) {
        log.debug("REST request to delete AuditProfileLogEntry : {}", id);
        auditProfileLogEntryRepository.delete(id);
        auditProfileLogEntrySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("auditProfileLogEntry", id.toString())).build();
    }

    /**
     * SEARCH  /_search/audit-profile-log-entries?query=:query : search for the auditProfileLogEntry corresponding
     * to the query.
     *
     * @param query the query of the auditProfileLogEntry search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/audit-profile-log-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuditProfileLogEntry>> searchAuditProfileLogEntries(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AuditProfileLogEntries for query {}", query);
        Page<AuditProfileLogEntry> page = auditProfileLogEntrySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/audit-profile-log-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
