package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Engagement;
import com.pwc.assurance.adc.repository.EngagementRepository;
import com.pwc.assurance.adc.service.EngagementService;
import com.pwc.assurance.adc.repository.search.EngagementSearchRepository;
import com.pwc.assurance.adc.service.dto.EngagementDTO;
import com.pwc.assurance.adc.service.mapper.EngagementMapper;

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

import com.pwc.assurance.adc.domain.enumeration.ResponseStatus;
/**
 * Test class for the EngagementResource REST controller.
 *
 * @see EngagementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class EngagementResourceIntTest {
    private static final String DEFAULT_FISCAL_YEAR = "AAAAA";
    private static final String UPDATED_FISCAL_YEAR = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ResponseStatus DEFAULT_STATUS = ResponseStatus.DRAFT;
    private static final ResponseStatus UPDATED_STATUS = ResponseStatus.FINAL;

    @Inject
    private EngagementRepository engagementRepository;

    @Inject
    private EngagementMapper engagementMapper;

    @Inject
    private EngagementService engagementService;

    @Inject
    private EngagementSearchRepository engagementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEngagementMockMvc;

    private Engagement engagement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EngagementResource engagementResource = new EngagementResource();
        ReflectionTestUtils.setField(engagementResource, "engagementService", engagementService);
        this.restEngagementMockMvc = MockMvcBuilders.standaloneSetup(engagementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Engagement createEntity(EntityManager em) {
        Engagement engagement = new Engagement();
        engagement = new Engagement()
                .fiscalYear(DEFAULT_FISCAL_YEAR)
                .description(DEFAULT_DESCRIPTION)
                .status(DEFAULT_STATUS);
        return engagement;
    }

    @Before
    public void initTest() {
        engagementSearchRepository.deleteAll();
        engagement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEngagement() throws Exception {
        int databaseSizeBeforeCreate = engagementRepository.findAll().size();

        // Create the Engagement
        EngagementDTO engagementDTO = engagementMapper.engagementToEngagementDTO(engagement);

        restEngagementMockMvc.perform(post("/api/engagements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engagementDTO)))
                .andExpect(status().isCreated());

        // Validate the Engagement in the database
        List<Engagement> engagements = engagementRepository.findAll();
        assertThat(engagements).hasSize(databaseSizeBeforeCreate + 1);
        Engagement testEngagement = engagements.get(engagements.size() - 1);
        assertThat(testEngagement.getFiscalYear()).isEqualTo(DEFAULT_FISCAL_YEAR);
        assertThat(testEngagement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEngagement.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Engagement in ElasticSearch
        Engagement engagementEs = engagementSearchRepository.findOne(testEngagement.getId());
        assertThat(engagementEs).isEqualToComparingFieldByField(testEngagement);
    }

    @Test
    @Transactional
    public void getAllEngagements() throws Exception {
        // Initialize the database
        engagementRepository.saveAndFlush(engagement);

        // Get all the engagements
        restEngagementMockMvc.perform(get("/api/engagements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(engagement.getId().intValue())))
                .andExpect(jsonPath("$.[*].fiscalYear").value(hasItem(DEFAULT_FISCAL_YEAR.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getEngagement() throws Exception {
        // Initialize the database
        engagementRepository.saveAndFlush(engagement);

        // Get the engagement
        restEngagementMockMvc.perform(get("/api/engagements/{id}", engagement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(engagement.getId().intValue()))
            .andExpect(jsonPath("$.fiscalYear").value(DEFAULT_FISCAL_YEAR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEngagement() throws Exception {
        // Get the engagement
        restEngagementMockMvc.perform(get("/api/engagements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEngagement() throws Exception {
        // Initialize the database
        engagementRepository.saveAndFlush(engagement);
        engagementSearchRepository.save(engagement);
        int databaseSizeBeforeUpdate = engagementRepository.findAll().size();

        // Update the engagement
        Engagement updatedEngagement = engagementRepository.findOne(engagement.getId());
        updatedEngagement
                .fiscalYear(UPDATED_FISCAL_YEAR)
                .description(UPDATED_DESCRIPTION)
                .status(UPDATED_STATUS);
        EngagementDTO engagementDTO = engagementMapper.engagementToEngagementDTO(updatedEngagement);

        restEngagementMockMvc.perform(put("/api/engagements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engagementDTO)))
                .andExpect(status().isOk());

        // Validate the Engagement in the database
        List<Engagement> engagements = engagementRepository.findAll();
        assertThat(engagements).hasSize(databaseSizeBeforeUpdate);
        Engagement testEngagement = engagements.get(engagements.size() - 1);
        assertThat(testEngagement.getFiscalYear()).isEqualTo(UPDATED_FISCAL_YEAR);
        assertThat(testEngagement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEngagement.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Engagement in ElasticSearch
        Engagement engagementEs = engagementSearchRepository.findOne(testEngagement.getId());
        assertThat(engagementEs).isEqualToComparingFieldByField(testEngagement);
    }

    @Test
    @Transactional
    public void deleteEngagement() throws Exception {
        // Initialize the database
        engagementRepository.saveAndFlush(engagement);
        engagementSearchRepository.save(engagement);
        int databaseSizeBeforeDelete = engagementRepository.findAll().size();

        // Get the engagement
        restEngagementMockMvc.perform(delete("/api/engagements/{id}", engagement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean engagementExistsInEs = engagementSearchRepository.exists(engagement.getId());
        assertThat(engagementExistsInEs).isFalse();

        // Validate the database is empty
        List<Engagement> engagements = engagementRepository.findAll();
        assertThat(engagements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEngagement() throws Exception {
        // Initialize the database
        engagementRepository.saveAndFlush(engagement);
        engagementSearchRepository.save(engagement);

        // Search the engagement
        restEngagementMockMvc.perform(get("/api/_search/engagements?query=id:" + engagement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engagement.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYear").value(hasItem(DEFAULT_FISCAL_YEAR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
