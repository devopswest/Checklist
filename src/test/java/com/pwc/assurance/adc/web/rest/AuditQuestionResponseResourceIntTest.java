package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.AuditQuestionResponse;
import com.pwc.assurance.adc.repository.AuditQuestionResponseRepository;
import com.pwc.assurance.adc.repository.search.AuditQuestionResponseSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuditQuestionResponseResource REST controller.
 *
 * @see AuditQuestionResponseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class AuditQuestionResponseResourceIntTest {
    private static final String DEFAULT_QUESTION_RESPONSE = "AAAAA";
    private static final String UPDATED_QUESTION_RESPONSE = "BBBBB";

    @Inject
    private AuditQuestionResponseRepository auditQuestionResponseRepository;

    @Inject
    private AuditQuestionResponseSearchRepository auditQuestionResponseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAuditQuestionResponseMockMvc;

    private AuditQuestionResponse auditQuestionResponse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuditQuestionResponseResource auditQuestionResponseResource = new AuditQuestionResponseResource();
        ReflectionTestUtils.setField(auditQuestionResponseResource, "auditQuestionResponseSearchRepository", auditQuestionResponseSearchRepository);
        ReflectionTestUtils.setField(auditQuestionResponseResource, "auditQuestionResponseRepository", auditQuestionResponseRepository);
        this.restAuditQuestionResponseMockMvc = MockMvcBuilders.standaloneSetup(auditQuestionResponseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditQuestionResponse createEntity(EntityManager em) {
        AuditQuestionResponse auditQuestionResponse = new AuditQuestionResponse();
        auditQuestionResponse = new AuditQuestionResponse();
        auditQuestionResponse.setQuestionResponse(DEFAULT_QUESTION_RESPONSE);
        return auditQuestionResponse;
    }

    @Before
    public void initTest() {
        auditQuestionResponseSearchRepository.deleteAll();
        auditQuestionResponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditQuestionResponse() throws Exception {
        int databaseSizeBeforeCreate = auditQuestionResponseRepository.findAll().size();

        // Create the AuditQuestionResponse

        restAuditQuestionResponseMockMvc.perform(post("/api/audit-question-responses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auditQuestionResponse)))
                .andExpect(status().isCreated());

        // Validate the AuditQuestionResponse in the database
        List<AuditQuestionResponse> auditQuestionResponses = auditQuestionResponseRepository.findAll();
        assertThat(auditQuestionResponses).hasSize(databaseSizeBeforeCreate + 1);
        AuditQuestionResponse testAuditQuestionResponse = auditQuestionResponses.get(auditQuestionResponses.size() - 1);
        assertThat(testAuditQuestionResponse.getQuestionResponse()).isEqualTo(DEFAULT_QUESTION_RESPONSE);

        // Validate the AuditQuestionResponse in ElasticSearch
        AuditQuestionResponse auditQuestionResponseEs = auditQuestionResponseSearchRepository.findOne(testAuditQuestionResponse.getId());
        assertThat(auditQuestionResponseEs).isEqualToComparingFieldByField(testAuditQuestionResponse);
    }

    @Test
    @Transactional
    public void getAllAuditQuestionResponses() throws Exception {
        // Initialize the database
        auditQuestionResponseRepository.saveAndFlush(auditQuestionResponse);

        // Get all the auditQuestionResponses
        restAuditQuestionResponseMockMvc.perform(get("/api/audit-question-responses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auditQuestionResponse.getId().intValue())))
                .andExpect(jsonPath("$.[*].questionResponse").value(hasItem(DEFAULT_QUESTION_RESPONSE.toString())));
    }

    @Test
    @Transactional
    public void getAuditQuestionResponse() throws Exception {
        // Initialize the database
        auditQuestionResponseRepository.saveAndFlush(auditQuestionResponse);

        // Get the auditQuestionResponse
        restAuditQuestionResponseMockMvc.perform(get("/api/audit-question-responses/{id}", auditQuestionResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auditQuestionResponse.getId().intValue()))
            .andExpect(jsonPath("$.questionResponse").value(DEFAULT_QUESTION_RESPONSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuditQuestionResponse() throws Exception {
        // Get the auditQuestionResponse
        restAuditQuestionResponseMockMvc.perform(get("/api/audit-question-responses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditQuestionResponse() throws Exception {
        // Initialize the database
        auditQuestionResponseRepository.saveAndFlush(auditQuestionResponse);
        auditQuestionResponseSearchRepository.save(auditQuestionResponse);
        int databaseSizeBeforeUpdate = auditQuestionResponseRepository.findAll().size();

        // Update the auditQuestionResponse
        AuditQuestionResponse updatedAuditQuestionResponse = auditQuestionResponseRepository.findOne(auditQuestionResponse.getId());
        updatedAuditQuestionResponse.setQuestionResponse(UPDATED_QUESTION_RESPONSE);

        restAuditQuestionResponseMockMvc.perform(put("/api/audit-question-responses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuditQuestionResponse)))
                .andExpect(status().isOk());

        // Validate the AuditQuestionResponse in the database
        List<AuditQuestionResponse> auditQuestionResponses = auditQuestionResponseRepository.findAll();
        assertThat(auditQuestionResponses).hasSize(databaseSizeBeforeUpdate);
        AuditQuestionResponse testAuditQuestionResponse = auditQuestionResponses.get(auditQuestionResponses.size() - 1);
        assertThat(testAuditQuestionResponse.getQuestionResponse()).isEqualTo(UPDATED_QUESTION_RESPONSE);

        // Validate the AuditQuestionResponse in ElasticSearch
        AuditQuestionResponse auditQuestionResponseEs = auditQuestionResponseSearchRepository.findOne(testAuditQuestionResponse.getId());
        assertThat(auditQuestionResponseEs).isEqualToComparingFieldByField(testAuditQuestionResponse);
    }

    @Test
    @Transactional
    public void deleteAuditQuestionResponse() throws Exception {
        // Initialize the database
        auditQuestionResponseRepository.saveAndFlush(auditQuestionResponse);
        auditQuestionResponseSearchRepository.save(auditQuestionResponse);
        int databaseSizeBeforeDelete = auditQuestionResponseRepository.findAll().size();

        // Get the auditQuestionResponse
        restAuditQuestionResponseMockMvc.perform(delete("/api/audit-question-responses/{id}", auditQuestionResponse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean auditQuestionResponseExistsInEs = auditQuestionResponseSearchRepository.exists(auditQuestionResponse.getId());
        assertThat(auditQuestionResponseExistsInEs).isFalse();

        // Validate the database is empty
        List<AuditQuestionResponse> auditQuestionResponses = auditQuestionResponseRepository.findAll();
        assertThat(auditQuestionResponses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAuditQuestionResponse() throws Exception {
        // Initialize the database
        auditQuestionResponseRepository.saveAndFlush(auditQuestionResponse);
        auditQuestionResponseSearchRepository.save(auditQuestionResponse);

        // Search the auditQuestionResponse
        restAuditQuestionResponseMockMvc.perform(get("/api/_search/audit-question-responses?query=id:" + auditQuestionResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditQuestionResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionResponse").value(hasItem(DEFAULT_QUESTION_RESPONSE.toString())));
    }
}
