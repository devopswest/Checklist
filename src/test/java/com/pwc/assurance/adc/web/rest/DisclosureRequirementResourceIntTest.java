package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.DisclosureRequirement;
import com.pwc.assurance.adc.repository.DisclosureRequirementRepository;
import com.pwc.assurance.adc.service.DisclosureRequirementService;
import com.pwc.assurance.adc.repository.search.DisclosureRequirementSearchRepository;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementDTO;
import com.pwc.assurance.adc.service.mapper.DisclosureRequirementMapper;

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
 * Test class for the DisclosureRequirementResource REST controller.
 *
 * @see DisclosureRequirementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class DisclosureRequirementResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "A";
    private static final String UPDATED_DESCRIPTION = "B";

    @Inject
    private DisclosureRequirementRepository disclosureRequirementRepository;

    @Inject
    private DisclosureRequirementMapper disclosureRequirementMapper;

    @Inject
    private DisclosureRequirementService disclosureRequirementService;

    @Inject
    private DisclosureRequirementSearchRepository disclosureRequirementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDisclosureRequirementMockMvc;

    private DisclosureRequirement disclosureRequirement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DisclosureRequirementResource disclosureRequirementResource = new DisclosureRequirementResource();
        ReflectionTestUtils.setField(disclosureRequirementResource, "disclosureRequirementService", disclosureRequirementService);
        this.restDisclosureRequirementMockMvc = MockMvcBuilders.standaloneSetup(disclosureRequirementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisclosureRequirement createEntity(EntityManager em) {
        DisclosureRequirement disclosureRequirement = new DisclosureRequirement();
        disclosureRequirement = new DisclosureRequirement()
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION);
        return disclosureRequirement;
    }

    @Before
    public void initTest() {
        disclosureRequirementSearchRepository.deleteAll();
        disclosureRequirement = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisclosureRequirement() throws Exception {
        int databaseSizeBeforeCreate = disclosureRequirementRepository.findAll().size();

        // Create the DisclosureRequirement
        DisclosureRequirementDTO disclosureRequirementDTO = disclosureRequirementMapper.disclosureRequirementToDisclosureRequirementDTO(disclosureRequirement);

        restDisclosureRequirementMockMvc.perform(post("/api/disclosure-requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disclosureRequirementDTO)))
                .andExpect(status().isCreated());

        // Validate the DisclosureRequirement in the database
        List<DisclosureRequirement> disclosureRequirements = disclosureRequirementRepository.findAll();
        assertThat(disclosureRequirements).hasSize(databaseSizeBeforeCreate + 1);
        DisclosureRequirement testDisclosureRequirement = disclosureRequirements.get(disclosureRequirements.size() - 1);
        assertThat(testDisclosureRequirement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDisclosureRequirement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the DisclosureRequirement in ElasticSearch
        DisclosureRequirement disclosureRequirementEs = disclosureRequirementSearchRepository.findOne(testDisclosureRequirement.getId());
        assertThat(disclosureRequirementEs).isEqualToComparingFieldByField(testDisclosureRequirement);
    }

    @Test
    @Transactional
    public void getAllDisclosureRequirements() throws Exception {
        // Initialize the database
        disclosureRequirementRepository.saveAndFlush(disclosureRequirement);

        // Get all the disclosureRequirements
        restDisclosureRequirementMockMvc.perform(get("/api/disclosure-requirements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(disclosureRequirement.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getDisclosureRequirement() throws Exception {
        // Initialize the database
        disclosureRequirementRepository.saveAndFlush(disclosureRequirement);

        // Get the disclosureRequirement
        restDisclosureRequirementMockMvc.perform(get("/api/disclosure-requirements/{id}", disclosureRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disclosureRequirement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDisclosureRequirement() throws Exception {
        // Get the disclosureRequirement
        restDisclosureRequirementMockMvc.perform(get("/api/disclosure-requirements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisclosureRequirement() throws Exception {
        // Initialize the database
        disclosureRequirementRepository.saveAndFlush(disclosureRequirement);
        disclosureRequirementSearchRepository.save(disclosureRequirement);
        int databaseSizeBeforeUpdate = disclosureRequirementRepository.findAll().size();

        // Update the disclosureRequirement
        DisclosureRequirement updatedDisclosureRequirement = disclosureRequirementRepository.findOne(disclosureRequirement.getId());
        updatedDisclosureRequirement
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION);
        DisclosureRequirementDTO disclosureRequirementDTO = disclosureRequirementMapper.disclosureRequirementToDisclosureRequirementDTO(updatedDisclosureRequirement);

        restDisclosureRequirementMockMvc.perform(put("/api/disclosure-requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disclosureRequirementDTO)))
                .andExpect(status().isOk());

        // Validate the DisclosureRequirement in the database
        List<DisclosureRequirement> disclosureRequirements = disclosureRequirementRepository.findAll();
        assertThat(disclosureRequirements).hasSize(databaseSizeBeforeUpdate);
        DisclosureRequirement testDisclosureRequirement = disclosureRequirements.get(disclosureRequirements.size() - 1);
        assertThat(testDisclosureRequirement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDisclosureRequirement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the DisclosureRequirement in ElasticSearch
        DisclosureRequirement disclosureRequirementEs = disclosureRequirementSearchRepository.findOne(testDisclosureRequirement.getId());
        assertThat(disclosureRequirementEs).isEqualToComparingFieldByField(testDisclosureRequirement);
    }

    @Test
    @Transactional
    public void deleteDisclosureRequirement() throws Exception {
        // Initialize the database
        disclosureRequirementRepository.saveAndFlush(disclosureRequirement);
        disclosureRequirementSearchRepository.save(disclosureRequirement);
        int databaseSizeBeforeDelete = disclosureRequirementRepository.findAll().size();

        // Get the disclosureRequirement
        restDisclosureRequirementMockMvc.perform(delete("/api/disclosure-requirements/{id}", disclosureRequirement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean disclosureRequirementExistsInEs = disclosureRequirementSearchRepository.exists(disclosureRequirement.getId());
        assertThat(disclosureRequirementExistsInEs).isFalse();

        // Validate the database is empty
        List<DisclosureRequirement> disclosureRequirements = disclosureRequirementRepository.findAll();
        assertThat(disclosureRequirements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDisclosureRequirement() throws Exception {
        // Initialize the database
        disclosureRequirementRepository.saveAndFlush(disclosureRequirement);
        disclosureRequirementSearchRepository.save(disclosureRequirement);

        // Search the disclosureRequirement
        restDisclosureRequirementMockMvc.perform(get("/api/_search/disclosure-requirements?query=id:" + disclosureRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disclosureRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
