package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.FeatureAuthorityService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.FeatureAuthorityDTO;
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
 * REST controller for managing FeatureAuthority.
 */
@RestController
@RequestMapping("/api")
public class FeatureAuthorityResource {

    private final Logger log = LoggerFactory.getLogger(FeatureAuthorityResource.class);
        
    @Inject
    private FeatureAuthorityService featureAuthorityService;

    /**
     * POST  /feature-authorities : Create a new featureAuthority.
     *
     * @param featureAuthorityDTO the featureAuthorityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new featureAuthorityDTO, or with status 400 (Bad Request) if the featureAuthority has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/feature-authorities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureAuthorityDTO> createFeatureAuthority(@RequestBody FeatureAuthorityDTO featureAuthorityDTO) throws URISyntaxException {
        log.debug("REST request to save FeatureAuthority : {}", featureAuthorityDTO);
        if (featureAuthorityDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("featureAuthority", "idexists", "A new featureAuthority cannot already have an ID")).body(null);
        }
        FeatureAuthorityDTO result = featureAuthorityService.save(featureAuthorityDTO);
        return ResponseEntity.created(new URI("/api/feature-authorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("featureAuthority", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /feature-authorities : Updates an existing featureAuthority.
     *
     * @param featureAuthorityDTO the featureAuthorityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated featureAuthorityDTO,
     * or with status 400 (Bad Request) if the featureAuthorityDTO is not valid,
     * or with status 500 (Internal Server Error) if the featureAuthorityDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/feature-authorities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureAuthorityDTO> updateFeatureAuthority(@RequestBody FeatureAuthorityDTO featureAuthorityDTO) throws URISyntaxException {
        log.debug("REST request to update FeatureAuthority : {}", featureAuthorityDTO);
        if (featureAuthorityDTO.getId() == null) {
            return createFeatureAuthority(featureAuthorityDTO);
        }
        FeatureAuthorityDTO result = featureAuthorityService.save(featureAuthorityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("featureAuthority", featureAuthorityDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /feature-authorities : get all the featureAuthorities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of featureAuthorities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/feature-authorities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FeatureAuthorityDTO>> getAllFeatureAuthorities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FeatureAuthorities");
        Page<FeatureAuthorityDTO> page = featureAuthorityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feature-authorities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /feature-authorities/:id : get the "id" featureAuthority.
     *
     * @param id the id of the featureAuthorityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the featureAuthorityDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/feature-authorities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureAuthorityDTO> getFeatureAuthority(@PathVariable Long id) {
        log.debug("REST request to get FeatureAuthority : {}", id);
        FeatureAuthorityDTO featureAuthorityDTO = featureAuthorityService.findOne(id);
        return Optional.ofNullable(featureAuthorityDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /feature-authorities/:id : delete the "id" featureAuthority.
     *
     * @param id the id of the featureAuthorityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/feature-authorities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFeatureAuthority(@PathVariable Long id) {
        log.debug("REST request to delete FeatureAuthority : {}", id);
        featureAuthorityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("featureAuthority", id.toString())).build();
    }

    /**
     * SEARCH  /_search/feature-authorities?query=:query : search for the featureAuthority corresponding
     * to the query.
     *
     * @param query the query of the featureAuthority search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/feature-authorities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FeatureAuthorityDTO>> searchFeatureAuthorities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of FeatureAuthorities for query {}", query);
        Page<FeatureAuthorityDTO> page = featureAuthorityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/feature-authorities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
