package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.NotificationActionService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.NotificationActionDTO;
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
 * REST controller for managing NotificationAction.
 */
@RestController
@RequestMapping("/api")
public class NotificationActionResource {

    private final Logger log = LoggerFactory.getLogger(NotificationActionResource.class);
        
    @Inject
    private NotificationActionService notificationActionService;

    /**
     * POST  /notification-actions : Create a new notificationAction.
     *
     * @param notificationActionDTO the notificationActionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationActionDTO, or with status 400 (Bad Request) if the notificationAction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-actions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationActionDTO> createNotificationAction(@RequestBody NotificationActionDTO notificationActionDTO) throws URISyntaxException {
        log.debug("REST request to save NotificationAction : {}", notificationActionDTO);
        if (notificationActionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notificationAction", "idexists", "A new notificationAction cannot already have an ID")).body(null);
        }
        NotificationActionDTO result = notificationActionService.save(notificationActionDTO);
        return ResponseEntity.created(new URI("/api/notification-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notificationAction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-actions : Updates an existing notificationAction.
     *
     * @param notificationActionDTO the notificationActionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationActionDTO,
     * or with status 400 (Bad Request) if the notificationActionDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificationActionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-actions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationActionDTO> updateNotificationAction(@RequestBody NotificationActionDTO notificationActionDTO) throws URISyntaxException {
        log.debug("REST request to update NotificationAction : {}", notificationActionDTO);
        if (notificationActionDTO.getId() == null) {
            return createNotificationAction(notificationActionDTO);
        }
        NotificationActionDTO result = notificationActionService.save(notificationActionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notificationAction", notificationActionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-actions : get all the notificationActions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationActions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/notification-actions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationActionDTO>> getAllNotificationActions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NotificationActions");
        Page<NotificationActionDTO> page = notificationActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-actions/:id : get the "id" notificationAction.
     *
     * @param id the id of the notificationActionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationActionDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notification-actions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationActionDTO> getNotificationAction(@PathVariable Long id) {
        log.debug("REST request to get NotificationAction : {}", id);
        NotificationActionDTO notificationActionDTO = notificationActionService.findOne(id);
        return Optional.ofNullable(notificationActionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notification-actions/:id : delete the "id" notificationAction.
     *
     * @param id the id of the notificationActionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notification-actions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotificationAction(@PathVariable Long id) {
        log.debug("REST request to delete NotificationAction : {}", id);
        notificationActionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notificationAction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-actions?query=:query : search for the notificationAction corresponding
     * to the query.
     *
     * @param query the query of the notificationAction search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/notification-actions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationActionDTO>> searchNotificationActions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of NotificationActions for query {}", query);
        Page<NotificationActionDTO> page = notificationActionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notification-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
