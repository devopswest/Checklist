package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ChecklistWorkflow;
import com.pwc.assurance.adc.repository.ChecklistWorkflowRepository;
import com.pwc.assurance.adc.service.ChecklistWorkflowService;
import com.pwc.assurance.adc.repository.search.ChecklistWorkflowSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistWorkflowDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistWorkflowMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pwc.assurance.adc.domain.enumeration.WorkflowTaskStatus;
/**
 * Test class for the ChecklistWorkflowResource REST controller.
 *
 * @see ChecklistWorkflowResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ChecklistWorkflowResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_HAPPENED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HAPPENED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HAPPENED_STR = dateTimeFormatter.format(DEFAULT_HAPPENED);

    private static final WorkflowTaskStatus DEFAULT_STATUS = WorkflowTaskStatus.PENDING;
    private static final WorkflowTaskStatus UPDATED_STATUS = WorkflowTaskStatus.COMPLETED;
    private static final String DEFAULT_COMMENTS = "AAAAA";
    private static final String UPDATED_COMMENTS = "BBBBB";

    @Inject
    private ChecklistWorkflowRepository checklistWorkflowRepository;

    @Inject
    private ChecklistWorkflowMapper checklistWorkflowMapper;

    @Inject
    private ChecklistWorkflowService checklistWorkflowService;

    @Inject
    private ChecklistWorkflowSearchRepository checklistWorkflowSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChecklistWorkflowMockMvc;

    private ChecklistWorkflow checklistWorkflow;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChecklistWorkflowResource checklistWorkflowResource = new ChecklistWorkflowResource();
        ReflectionTestUtils.setField(checklistWorkflowResource, "checklistWorkflowService", checklistWorkflowService);
        this.restChecklistWorkflowMockMvc = MockMvcBuilders.standaloneSetup(checklistWorkflowResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChecklistWorkflow createEntity(EntityManager em) {
        ChecklistWorkflow checklistWorkflow = new ChecklistWorkflow();
        checklistWorkflow = new ChecklistWorkflow()
                .happened(DEFAULT_HAPPENED)
                .status(DEFAULT_STATUS)
                .comments(DEFAULT_COMMENTS);
        return checklistWorkflow;
    }

    @Before
    public void initTest() {
        checklistWorkflowSearchRepository.deleteAll();
        checklistWorkflow = createEntity(em);
    }

    @Test
    @Transactional
    public void createChecklistWorkflow() throws Exception {
        int databaseSizeBeforeCreate = checklistWorkflowRepository.findAll().size();

        // Create the ChecklistWorkflow
        ChecklistWorkflowDTO checklistWorkflowDTO = checklistWorkflowMapper.checklistWorkflowToChecklistWorkflowDTO(checklistWorkflow);

        restChecklistWorkflowMockMvc.perform(post("/api/checklist-workflows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistWorkflowDTO)))
                .andExpect(status().isCreated());

        // Validate the ChecklistWorkflow in the database
        List<ChecklistWorkflow> checklistWorkflows = checklistWorkflowRepository.findAll();
        assertThat(checklistWorkflows).hasSize(databaseSizeBeforeCreate + 1);
        ChecklistWorkflow testChecklistWorkflow = checklistWorkflows.get(checklistWorkflows.size() - 1);
        assertThat(testChecklistWorkflow.getHappened()).isEqualTo(DEFAULT_HAPPENED);
        assertThat(testChecklistWorkflow.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testChecklistWorkflow.getComments()).isEqualTo(DEFAULT_COMMENTS);

        // Validate the ChecklistWorkflow in ElasticSearch
        ChecklistWorkflow checklistWorkflowEs = checklistWorkflowSearchRepository.findOne(testChecklistWorkflow.getId());
        assertThat(checklistWorkflowEs).isEqualToComparingFieldByField(testChecklistWorkflow);
    }

    @Test
    @Transactional
    public void getAllChecklistWorkflows() throws Exception {
        // Initialize the database
        checklistWorkflowRepository.saveAndFlush(checklistWorkflow);

        // Get all the checklistWorkflows
        restChecklistWorkflowMockMvc.perform(get("/api/checklist-workflows?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(checklistWorkflow.getId().intValue())))
                .andExpect(jsonPath("$.[*].happened").value(hasItem(DEFAULT_HAPPENED_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void getChecklistWorkflow() throws Exception {
        // Initialize the database
        checklistWorkflowRepository.saveAndFlush(checklistWorkflow);

        // Get the checklistWorkflow
        restChecklistWorkflowMockMvc.perform(get("/api/checklist-workflows/{id}", checklistWorkflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checklistWorkflow.getId().intValue()))
            .andExpect(jsonPath("$.happened").value(DEFAULT_HAPPENED_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChecklistWorkflow() throws Exception {
        // Get the checklistWorkflow
        restChecklistWorkflowMockMvc.perform(get("/api/checklist-workflows/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChecklistWorkflow() throws Exception {
        // Initialize the database
        checklistWorkflowRepository.saveAndFlush(checklistWorkflow);
        checklistWorkflowSearchRepository.save(checklistWorkflow);
        int databaseSizeBeforeUpdate = checklistWorkflowRepository.findAll().size();

        // Update the checklistWorkflow
        ChecklistWorkflow updatedChecklistWorkflow = checklistWorkflowRepository.findOne(checklistWorkflow.getId());
        updatedChecklistWorkflow
                .happened(UPDATED_HAPPENED)
                .status(UPDATED_STATUS)
                .comments(UPDATED_COMMENTS);
        ChecklistWorkflowDTO checklistWorkflowDTO = checklistWorkflowMapper.checklistWorkflowToChecklistWorkflowDTO(updatedChecklistWorkflow);

        restChecklistWorkflowMockMvc.perform(put("/api/checklist-workflows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistWorkflowDTO)))
                .andExpect(status().isOk());

        // Validate the ChecklistWorkflow in the database
        List<ChecklistWorkflow> checklistWorkflows = checklistWorkflowRepository.findAll();
        assertThat(checklistWorkflows).hasSize(databaseSizeBeforeUpdate);
        ChecklistWorkflow testChecklistWorkflow = checklistWorkflows.get(checklistWorkflows.size() - 1);
        assertThat(testChecklistWorkflow.getHappened()).isEqualTo(UPDATED_HAPPENED);
        assertThat(testChecklistWorkflow.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testChecklistWorkflow.getComments()).isEqualTo(UPDATED_COMMENTS);

        // Validate the ChecklistWorkflow in ElasticSearch
        ChecklistWorkflow checklistWorkflowEs = checklistWorkflowSearchRepository.findOne(testChecklistWorkflow.getId());
        assertThat(checklistWorkflowEs).isEqualToComparingFieldByField(testChecklistWorkflow);
    }

    @Test
    @Transactional
    public void deleteChecklistWorkflow() throws Exception {
        // Initialize the database
        checklistWorkflowRepository.saveAndFlush(checklistWorkflow);
        checklistWorkflowSearchRepository.save(checklistWorkflow);
        int databaseSizeBeforeDelete = checklistWorkflowRepository.findAll().size();

        // Get the checklistWorkflow
        restChecklistWorkflowMockMvc.perform(delete("/api/checklist-workflows/{id}", checklistWorkflow.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean checklistWorkflowExistsInEs = checklistWorkflowSearchRepository.exists(checklistWorkflow.getId());
        assertThat(checklistWorkflowExistsInEs).isFalse();

        // Validate the database is empty
        List<ChecklistWorkflow> checklistWorkflows = checklistWorkflowRepository.findAll();
        assertThat(checklistWorkflows).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChecklistWorkflow() throws Exception {
        // Initialize the database
        checklistWorkflowRepository.saveAndFlush(checklistWorkflow);
        checklistWorkflowSearchRepository.save(checklistWorkflow);

        // Search the checklistWorkflow
        restChecklistWorkflowMockMvc.perform(get("/api/_search/checklist-workflows?query=id:" + checklistWorkflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checklistWorkflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].happened").value(hasItem(DEFAULT_HAPPENED_STR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }
}
