package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.service.FeatureService;
import com.pwc.assurance.adc.web.rest.util.HeaderUtil;
import com.pwc.assurance.adc.web.rest.util.PaginationUtil;
import com.pwc.assurance.adc.service.dto.FeatureDTO;
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
 * REST controller for managing Feature.
 */
@RestController
@RequestMapping("/api")
public class FeatureResource {

    private final Logger log = LoggerFactory.getLogger(FeatureResource.class);
        
    @Inject
    private FeatureService featureService;

    /**
     * POST  /features : Create a new feature.
     *
     * @param featureDTO the featureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new featureDTO, or with status 400 (Bad Request) if the feature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/features",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureDTO> createFeature(@Valid @RequestBody FeatureDTO featureDTO) throws URISyntaxException {
        log.debug("REST request to save Feature : {}", featureDTO);
        if (featureDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("feature", "idexists", "A new feature cannot already have an ID")).body(null);
        }
        FeatureDTO result = featureService.save(featureDTO);
        return ResponseEntity.created(new URI("/api/features/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("feature", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /features : Updates an existing feature.
     *
     * @param featureDTO the featureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated featureDTO,
     * or with status 400 (Bad Request) if the featureDTO is not valid,
     * or with status 500 (Internal Server Error) if the featureDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/features",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureDTO> updateFeature(@Valid @RequestBody FeatureDTO featureDTO) throws URISyntaxException {
        log.debug("REST request to update Feature : {}", featureDTO);
        if (featureDTO.getId() == null) {
            return createFeature(featureDTO);
        }
        FeatureDTO result = featureService.save(featureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("feature", featureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /features : get all the features.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of features in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/features",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FeatureDTO>> getAllFeatures(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Features");
        Page<FeatureDTO> page = featureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/features");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /features/:id : get the "id" feature.
     *
     * @param id the id of the featureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the featureDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/features/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureDTO> getFeature(@PathVariable Long id) {
        log.debug("REST request to get Feature : {}", id);
        FeatureDTO featureDTO = featureService.findOne(id);
        return Optional.ofNullable(featureDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /features/:id : delete the "id" feature.
     *
     * @param id the id of the featureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/features/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        log.debug("REST request to delete Feature : {}", id);
        featureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("feature", id.toString())).build();
    }

    /**
     * SEARCH  /_search/features?query=:query : search for the feature corresponding
     * to the query.
     *
     * @param query the query of the feature search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/features",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FeatureDTO>> searchFeatures(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Features for query {}", query);
        Page<FeatureDTO> page = featureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/features");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
