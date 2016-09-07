package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.WorkflowStep;
import com.pwc.assurance.adc.repository.WorkflowStepRepository;
import com.pwc.assurance.adc.repository.search.WorkflowStepSearchRepository;

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

import com.pwc.assurance.adc.domain.enumeration.ApplicationAuthorities;
/**
 * Test class for the WorkflowStepResource REST controller.
 *
 * @see WorkflowStepResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class WorkflowStepResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ApplicationAuthorities DEFAULT_AUTHORITY = ApplicationAuthorities.ROLE_ADMIN;
    private static final ApplicationAuthorities UPDATED_AUTHORITY = ApplicationAuthorities.ROLE_USER;

    @Inject
    private WorkflowStepRepository workflowStepRepository;

    @Inject
    private WorkflowStepSearchRepository workflowStepSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWorkflowStepMockMvc;

    private WorkflowStep workflowStep;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkflowStepResource workflowStepResource = new WorkflowStepResource();
        ReflectionTestUtils.setField(workflowStepResource, "workflowStepSearchRepository", workflowStepSearchRepository);
        ReflectionTestUtils.setField(workflowStepResource, "workflowStepRepository", workflowStepRepository);
        this.restWorkflowStepMockMvc = MockMvcBuilders.standaloneSetup(workflowStepResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowStep createEntity(EntityManager em) {
        WorkflowStep workflowStep = new WorkflowStep();
        workflowStep = new WorkflowStep()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .authority(DEFAULT_AUTHORITY);
        return workflowStep;
    }

    @Before
    public void initTest() {
        workflowStepSearchRepository.deleteAll();
        workflowStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkflowStep() throws Exception {
        int databaseSizeBeforeCreate = workflowStepRepository.findAll().size();

        // Create the WorkflowStep

        restWorkflowStepMockMvc.perform(post("/api/workflow-steps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workflowStep)))
                .andExpect(status().isCreated());

        // Validate the WorkflowStep in the database
        List<WorkflowStep> workflowSteps = workflowStepRepository.findAll();
        assertThat(workflowSteps).hasSize(databaseSizeBeforeCreate + 1);
        WorkflowStep testWorkflowStep = workflowSteps.get(workflowSteps.size() - 1);
        assertThat(testWorkflowStep.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkflowStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkflowStep.getAuthority()).isEqualTo(DEFAULT_AUTHORITY);

        // Validate the WorkflowStep in ElasticSearch
        WorkflowStep workflowStepEs = workflowStepSearchRepository.findOne(testWorkflowStep.getId());
        assertThat(workflowStepEs).isEqualToComparingFieldByField(testWorkflowStep);
    }

    @Test
    @Transactional
    public void getAllWorkflowSteps() throws Exception {
        // Initialize the database
        workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowSteps
        restWorkflowStepMockMvc.perform(get("/api/workflow-steps?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));
    }

    @Test
    @Transactional
    public void getWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepRepository.saveAndFlush(workflowStep);

        // Get the workflowStep
        restWorkflowStepMockMvc.perform(get("/api/workflow-steps/{id}", workflowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workflowStep.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.authority").value(DEFAULT_AUTHORITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkflowStep() throws Exception {
        // Get the workflowStep
        restWorkflowStepMockMvc.perform(get("/api/workflow-steps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepRepository.saveAndFlush(workflowStep);
        workflowStepSearchRepository.save(workflowStep);
        int databaseSizeBeforeUpdate = workflowStepRepository.findAll().size();

        // Update the workflowStep
        WorkflowStep updatedWorkflowStep = workflowStepRepository.findOne(workflowStep.getId());
        updatedWorkflowStep
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .authority(UPDATED_AUTHORITY);

        restWorkflowStepMockMvc.perform(put("/api/workflow-steps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkflowStep)))
                .andExpect(status().isOk());

        // Validate the WorkflowStep in the database
        List<WorkflowStep> workflowSteps = workflowStepRepository.findAll();
        assertThat(workflowSteps).hasSize(databaseSizeBeforeUpdate);
        WorkflowStep testWorkflowStep = workflowSteps.get(workflowSteps.size() - 1);
        assertThat(testWorkflowStep.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkflowStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkflowStep.getAuthority()).isEqualTo(UPDATED_AUTHORITY);

        // Validate the WorkflowStep in ElasticSearch
        WorkflowStep workflowStepEs = workflowStepSearchRepository.findOne(testWorkflowStep.getId());
        assertThat(workflowStepEs).isEqualToComparingFieldByField(testWorkflowStep);
    }

    @Test
    @Transactional
    public void deleteWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepRepository.saveAndFlush(workflowStep);
        workflowStepSearchRepository.save(workflowStep);
        int databaseSizeBeforeDelete = workflowStepRepository.findAll().size();

        // Get the workflowStep
        restWorkflowStepMockMvc.perform(delete("/api/workflow-steps/{id}", workflowStep.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workflowStepExistsInEs = workflowStepSearchRepository.exists(workflowStep.getId());
        assertThat(workflowStepExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkflowStep> workflowSteps = workflowStepRepository.findAll();
        assertThat(workflowSteps).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepRepository.saveAndFlush(workflowStep);
        workflowStepSearchRepository.save(workflowStep);

        // Search the workflowStep
        restWorkflowStepMockMvc.perform(get("/api/_search/workflow-steps?query=id:" + workflowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));
    }
}
