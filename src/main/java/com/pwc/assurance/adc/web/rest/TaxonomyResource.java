package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.Taxonomy;

import com.pwc.assurance.adc.repository.TaxonomyRepository;
import com.pwc.assurance.adc.repository.search.TaxonomySearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Taxonomy.
 */
@RestController
@RequestMapping("/api")
public class TaxonomyResource {

    private final Logger log = LoggerFactory.getLogger(TaxonomyResource.class);
        
    @Inject
    private TaxonomyRepository taxonomyRepository;

    @Inject
    private TaxonomySearchRepository taxonomySearchRepository;

    /**
     * POST  /taxonomies : Create a new taxonomy.
     *
     * @param taxonomy the taxonomy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taxonomy, or with status 400 (Bad Request) if the taxonomy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/taxonomies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Taxonomy> createTaxonomy(@Valid @RequestBody Taxonomy taxonomy) throws URISyntaxException {
        log.debug("REST request to save Taxonomy : {}", taxonomy);
        if (taxonomy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("taxonomy", "idexists", "A new taxonomy cannot already have an ID")).body(null);
        }
        Taxonomy result = taxonomyRepository.save(taxonomy);
        taxonomySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/taxonomies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taxonomy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taxonomies : Updates an existing taxonomy.
     *
     * @param taxonomy the taxonomy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taxonomy,
     * or with status 400 (Bad Request) if the taxonomy is not valid,
     * or with status 500 (Internal Server Error) if the taxonomy couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/taxonomies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Taxonomy> updateTaxonomy(@Valid @RequestBody Taxonomy taxonomy) throws URISyntaxException {
        log.debug("REST request to update Taxonomy : {}", taxonomy);
        if (taxonomy.getId() == null) {
            return createTaxonomy(taxonomy);
        }
        Taxonomy result = taxonomyRepository.save(taxonomy);
        taxonomySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taxonomy", taxonomy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taxonomies : get all the taxonomies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taxonomies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/taxonomies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Taxonomy>> getAllTaxonomies(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Taxonomies");
        Page<Taxonomy> page = taxonomyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/taxonomies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /taxonomies/:id : get the "id" taxonomy.
     *
     * @param id the id of the taxonomy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taxonomy, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/taxonomies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Taxonomy> getTaxonomy(@PathVariable Long id) {
        log.debug("REST request to get Taxonomy : {}", id);
        Taxonomy taxonomy = taxonomyRepository.findOne(id);
        return Optional.ofNullable(taxonomy)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taxonomies/:id : delete the "id" taxonomy.
     *
     * @param id the id of the taxonomy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/taxonomies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaxonomy(@PathVariable Long id) {
        log.debug("REST request to delete Taxonomy : {}", id);
        taxonomyRepository.delete(id);
        taxonomySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taxonomy", id.toString())).build();
    }

    /**
     * SEARCH  /_search/taxonomies?query=:query : search for the taxonomy corresponding
     * to the query.
     *
     * @param query the query of the taxonomy search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/taxonomies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Taxonomy>> searchTaxonomies(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Taxonomies for query {}", query);
        Page<Taxonomy> page = taxonomySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/taxonomies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /taxonomies/parent/:code : get the "code" taxonomy.
     * 
     * @param code the code of the taxonomy parent
     * @return list of taxonomies associated with the parent
     */
    @RequestMapping(value = "/taxonomies/parent/{code}",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Taxonomy>> getTaxonomiesByParent(@PathVariable String code) {
    	log.debug("REST request to get the taxonomies list by parent code");
    	List<Taxonomy> taxonomies = taxonomyRepository.getTaxonomiesByParent(code);
    	return new ResponseEntity<>(taxonomies, HttpStatus.OK);
    }


}
