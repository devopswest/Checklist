package com.pwc.assurance.adc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pwc.assurance.adc.domain.Checklist;

import com.pwc.assurance.adc.repository.ChecklistRepository;
import com.pwc.assurance.adc.repository.search.ChecklistSearchRepository;
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
 * REST controller for managing Checklist.
 */
@RestController
@RequestMapping("/api")
public class ChecklistResourceExtended {

    private final Logger log = LoggerFactory.getLogger(ChecklistResource.class);

    @Inject
    private ChecklistRepository checklistRepository;

    @Inject
    private ChecklistSearchRepository checklistSearchRepository;





    /**
     * GET  /checklists/:id : get the "id" checklist.
     *
     * @param id the id of the checklist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checklist, or with status 404 (Not Found)
     */

/*Sample
{
  "id": 1008,
  "name": "Smaller Registrants (Form 10-K) and Other Public Entities",
  "description": null,
  "version": "1.0.0",
  "status": "RELEASED",
  "country": {
    "id": 1001,
    "code": "US",
    "name": "United States"
  },
  "questions":[
     {
       "id":9901,
       "question":"Some question",
       "paren": null,
       "questions" : [
       {
       "id":9903,
       "question":"Some question",
       "parent" : 9901
     }

       ]

     },
     {
       "id":9902,
       "question":"Some question"
     }

  ]
}
*/



    @RequestMapping(value = "/checklistsandquestions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Checklist> getChecklist(@PathVariable Long id) {
        log.debug("REST request to get Checklist : {}", id);
        Checklist checklist = checklistRepository.findOne(id);

        //TODO: Add ChecklistQuestions SET as part of the returned data

        return Optional.ofNullable(checklist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * POST  /checklists : Create a new checklist.
     *
     * @param checklist the checklist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checklist, or with status 400 (Bad Request) if the checklist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklistswithquestions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Checklist> createChecklist(@RequestBody Checklist checklist) throws URISyntaxException {
        log.debug("REST request to save Checklist : {}", checklist);
        if (checklist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checklist", "idexists", "A new checklist cannot already have an ID")).body(null);
        }
        Checklist result = checklistRepository.save(checklist);

        //TODO: Call the checklistQuestionRepositoty to save the questions

        checklistSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/checklists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checklist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checklists : Updates an existing checklist.
     *
     * @param checklist the checklist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checklist,
     * or with status 400 (Bad Request) if the checklist is not valid,
     * or with status 500 (Internal Server Error) if the checklist couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/checklistswithquestions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Checklist> updateChecklist(@RequestBody Checklist checklist) throws URISyntaxException {
        log.debug("REST request to update Checklist : {}", checklist);
        if (checklist.getId() == null) {
            return createChecklist(checklist);
        }
        Checklist result = checklistRepository.save(checklist);
        checklistSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checklist", checklist.getId().toString()))
            .body(result);
    }


}
