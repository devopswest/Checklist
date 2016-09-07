package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.Template;

import com.pwc.assurance.adc.repository.TemplateRepository;
import com.pwc.assurance.adc.repository.search.TemplateSearchRepository;
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
 * REST controller for managing Template.
 */
@RestController
@RequestMapping("/api")
public class TemplateResource {

    private final Logger log = LoggerFactory.getLogger(TemplateResource.class);
        
    @Inject
    private TemplateRepository templateRepository;

    @Inject
    private TemplateSearchRepository templateSearchRepository;

    /**
     * POST  /templates : Create a new template.
     *
     * @param template the template to create
     * @return the ResponseEntity with status 201 (Created) and with body the new template, or with status 400 (Bad Request) if the template has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/templates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Template> createTemplate(@Valid @RequestBody Template template) throws URISyntaxException {
        log.debug("REST request to save Template : {}", template);
        if (template.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("template", "idexists", "A new template cannot already have an ID")).body(null);
        }
        Template result = templateRepository.save(template);
        templateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("template", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /templates : Updates an existing template.
     *
     * @param template the template to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated template,
     * or with status 400 (Bad Request) if the template is not valid,
     * or with status 500 (Internal Server Error) if the template couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/templates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Template> updateTemplate(@Valid @RequestBody Template template) throws URISyntaxException {
        log.debug("REST request to update Template : {}", template);
        if (template.getId() == null) {
            return createTemplate(template);
        }
        Template result = templateRepository.save(template);
        templateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("template", template.getId().toString()))
            .body(result);
    }

    /**
     * GET  /templates : get all the templates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of templates in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Template>> getAllTemplates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Templates");
        Page<Template> page = templateRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /templates/:id : get the "id" template.
     *
     * @param id the id of the template to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the template, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/templates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Template> getTemplate(@PathVariable Long id) {
        log.debug("REST request to get Template : {}", id);
        Template template = templateRepository.findOne(id);
        return Optional.ofNullable(template)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /templates/:id : delete the "id" template.
     *
     * @param id the id of the template to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/templates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        log.debug("REST request to delete Template : {}", id);
        templateRepository.delete(id);
        templateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("template", id.toString())).build();
    }

    /**
     * SEARCH  /_search/templates?query=:query : search for the template corresponding
     * to the query.
     *
     * @param query the query of the template search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Template>> searchTemplates(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Templates for query {}", query);
        Page<Template> page = templateSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
