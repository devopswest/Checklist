package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Workflow;
import com.pwc.assurance.adc.repository.WorkflowRepository;
import com.pwc.assurance.adc.repository.search.WorkflowSearchRepository;

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
 * Test class for the WorkflowResource REST controller.
 *
 * @see WorkflowResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class WorkflowResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private WorkflowRepository workflowRepository;

    @Inject
    private WorkflowSearchRepository workflowSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWorkflowMockMvc;

    private Workflow workflow;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkflowResource workflowResource = new WorkflowResource();
        ReflectionTestUtils.setField(workflowResource, "workflowSearchRepository", workflowSearchRepository);
        ReflectionTestUtils.setField(workflowResource, "workflowRepository", workflowRepository);
        this.restWorkflowMockMvc = MockMvcBuilders.standaloneSetup(workflowResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workflow createEntity(EntityManager em) {
        Workflow workflow = new Workflow();
        workflow = new Workflow()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return workflow;
    }

    @Before
    public void initTest() {
        workflowSearchRepository.deleteAll();
        workflow = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkflow() throws Exception {
        int databaseSizeBeforeCreate = workflowRepository.findAll().size();

        // Create the Workflow

        restWorkflowMockMvc.perform(post("/api/workflows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workflow)))
                .andExpect(status().isCreated());

        // Validate the Workflow in the database
        List<Workflow> workflows = workflowRepository.findAll();
        assertThat(workflows).hasSize(databaseSizeBeforeCreate + 1);
        Workflow testWorkflow = workflows.get(workflows.size() - 1);
        assertThat(testWorkflow.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkflow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Workflow in ElasticSearch
        Workflow workflowEs = workflowSearchRepository.findOne(testWorkflow.getId());
        assertThat(workflowEs).isEqualToComparingFieldByField(testWorkflow);
    }

    @Test
    @Transactional
    public void getAllWorkflows() throws Exception {
        // Initialize the database
        workflowRepository.saveAndFlush(workflow);

        // Get all the workflows
        restWorkflowMockMvc.perform(get("/api/workflows?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workflow.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getWorkflow() throws Exception {
        // Initialize the database
        workflowRepository.saveAndFlush(workflow);

        // Get the workflow
        restWorkflowMockMvc.perform(get("/api/workflows/{id}", workflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workflow.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkflow() throws Exception {
        // Get the workflow
        restWorkflowMockMvc.perform(get("/api/workflows/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkflow() throws Exception {
        // Initialize the database
        workflowRepository.saveAndFlush(workflow);
        workflowSearchRepository.save(workflow);
        int databaseSizeBeforeUpdate = workflowRepository.findAll().size();

        // Update the workflow
        Workflow updatedWorkflow = workflowRepository.findOne(workflow.getId());
        updatedWorkflow
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION);

        restWorkflowMockMvc.perform(put("/api/workflows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkflow)))
                .andExpect(status().isOk());

        // Validate the Workflow in the database
        List<Workflow> workflows = workflowRepository.findAll();
        assertThat(workflows).hasSize(databaseSizeBeforeUpdate);
        Workflow testWorkflow = workflows.get(workflows.size() - 1);
        assertThat(testWorkflow.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkflow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Workflow in ElasticSearch
        Workflow workflowEs = workflowSearchRepository.findOne(testWorkflow.getId());
        assertThat(workflowEs).isEqualToComparingFieldByField(testWorkflow);
    }

    @Test
    @Transactional
    public void deleteWorkflow() throws Exception {
        // Initialize the database
        workflowRepository.saveAndFlush(workflow);
        workflowSearchRepository.save(workflow);
        int databaseSizeBeforeDelete = workflowRepository.findAll().size();

        // Get the workflow
        restWorkflowMockMvc.perform(delete("/api/workflows/{id}", workflow.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workflowExistsInEs = workflowSearchRepository.exists(workflow.getId());
        assertThat(workflowExistsInEs).isFalse();

        // Validate the database is empty
        List<Workflow> workflows = workflowRepository.findAll();
        assertThat(workflows).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkflow() throws Exception {
        // Initialize the database
        workflowRepository.saveAndFlush(workflow);
        workflowSearchRepository.save(workflow);

        // Search the workflow
        restWorkflowMockMvc.perform(get("/api/_search/workflows?query=id:" + workflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
