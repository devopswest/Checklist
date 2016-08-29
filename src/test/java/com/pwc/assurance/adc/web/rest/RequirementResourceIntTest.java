package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Requirement;
import com.pwc.assurance.adc.repository.RequirementRepository;
import com.pwc.assurance.adc.repository.search.RequirementSearchRepository;

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
 * Test class for the RequirementResource REST controller.
 *
 * @see RequirementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class RequirementResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private RequirementSearchRepository requirementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRequirementMockMvc;

    private Requirement requirement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequirementResource requirementResource = new RequirementResource();
        ReflectionTestUtils.setField(requirementResource, "requirementSearchRepository", requirementSearchRepository);
        ReflectionTestUtils.setField(requirementResource, "requirementRepository", requirementRepository);
        this.restRequirementMockMvc = MockMvcBuilders.standaloneSetup(requirementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requirement createEntity(EntityManager em) {
        Requirement requirement = new Requirement();
        requirement = new Requirement()
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION);
        return requirement;
    }

    @Before
    public void initTest() {
        requirementSearchRepository.deleteAll();
        requirement = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequirement() throws Exception {
        int databaseSizeBeforeCreate = requirementRepository.findAll().size();

        // Create the Requirement

        restRequirementMockMvc.perform(post("/api/requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requirement)))
                .andExpect(status().isCreated());

        // Validate the Requirement in the database
        List<Requirement> requirements = requirementRepository.findAll();
        assertThat(requirements).hasSize(databaseSizeBeforeCreate + 1);
        Requirement testRequirement = requirements.get(requirements.size() - 1);
        assertThat(testRequirement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRequirement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Requirement in ElasticSearch
        Requirement requirementEs = requirementSearchRepository.findOne(testRequirement.getId());
        assertThat(requirementEs).isEqualToComparingFieldByField(testRequirement);
    }

    @Test
    @Transactional
    public void getAllRequirements() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get all the requirements
        restRequirementMockMvc.perform(get("/api/requirements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(requirement.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", requirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requirement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequirement() throws Exception {
        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);
        requirementSearchRepository.save(requirement);
        int databaseSizeBeforeUpdate = requirementRepository.findAll().size();

        // Update the requirement
        Requirement updatedRequirement = requirementRepository.findOne(requirement.getId());
        updatedRequirement
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION);

        restRequirementMockMvc.perform(put("/api/requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRequirement)))
                .andExpect(status().isOk());

        // Validate the Requirement in the database
        List<Requirement> requirements = requirementRepository.findAll();
        assertThat(requirements).hasSize(databaseSizeBeforeUpdate);
        Requirement testRequirement = requirements.get(requirements.size() - 1);
        assertThat(testRequirement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRequirement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Requirement in ElasticSearch
        Requirement requirementEs = requirementSearchRepository.findOne(testRequirement.getId());
        assertThat(requirementEs).isEqualToComparingFieldByField(testRequirement);
    }

    @Test
    @Transactional
    public void deleteRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);
        requirementSearchRepository.save(requirement);
        int databaseSizeBeforeDelete = requirementRepository.findAll().size();

        // Get the requirement
        restRequirementMockMvc.perform(delete("/api/requirements/{id}", requirement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean requirementExistsInEs = requirementSearchRepository.exists(requirement.getId());
        assertThat(requirementExistsInEs).isFalse();

        // Validate the database is empty
        List<Requirement> requirements = requirementRepository.findAll();
        assertThat(requirements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);
        requirementSearchRepository.save(requirement);

        // Search the requirement
        restRequirementMockMvc.perform(get("/api/_search/requirements?query=id:" + requirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
