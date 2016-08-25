package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.AuditProfile;
import com.pwc.assurance.adc.repository.AuditProfileRepository;
import com.pwc.assurance.adc.repository.search.AuditProfileSearchRepository;

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
 * Test class for the AuditProfileResource REST controller.
 *
 * @see AuditProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class AuditProfileResourceIntTest {
    private static final String DEFAULT_FISCAL_YEAR = "AAAAA";
    private static final String UPDATED_FISCAL_YEAR = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ResponseStatus DEFAULT_STATUS = ResponseStatus.DRAFT;
    private static final ResponseStatus UPDATED_STATUS = ResponseStatus.FINISH;

    @Inject
    private AuditProfileRepository auditProfileRepository;

    @Inject
    private AuditProfileSearchRepository auditProfileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAuditProfileMockMvc;

    private AuditProfile auditProfile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuditProfileResource auditProfileResource = new AuditProfileResource();
        ReflectionTestUtils.setField(auditProfileResource, "auditProfileSearchRepository", auditProfileSearchRepository);
        ReflectionTestUtils.setField(auditProfileResource, "auditProfileRepository", auditProfileRepository);
        this.restAuditProfileMockMvc = MockMvcBuilders.standaloneSetup(auditProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditProfile createEntity(EntityManager em) {
        AuditProfile auditProfile = new AuditProfile();
        auditProfile = new AuditProfile();
        auditProfile.setFiscalYear(DEFAULT_FISCAL_YEAR);
        auditProfile.setDescription(DEFAULT_DESCRIPTION);
        auditProfile.setStatus(DEFAULT_STATUS);
        return auditProfile;
    }

    @Before
    public void initTest() {
        auditProfileSearchRepository.deleteAll();
        auditProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditProfile() throws Exception {
        int databaseSizeBeforeCreate = auditProfileRepository.findAll().size();

        // Create the AuditProfile

        restAuditProfileMockMvc.perform(post("/api/audit-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auditProfile)))
                .andExpect(status().isCreated());

        // Validate the AuditProfile in the database
        List<AuditProfile> auditProfiles = auditProfileRepository.findAll();
        assertThat(auditProfiles).hasSize(databaseSizeBeforeCreate + 1);
        AuditProfile testAuditProfile = auditProfiles.get(auditProfiles.size() - 1);
        assertThat(testAuditProfile.getFiscalYear()).isEqualTo(DEFAULT_FISCAL_YEAR);
        assertThat(testAuditProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAuditProfile.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the AuditProfile in ElasticSearch
        AuditProfile auditProfileEs = auditProfileSearchRepository.findOne(testAuditProfile.getId());
        assertThat(auditProfileEs).isEqualToComparingFieldByField(testAuditProfile);
    }

    @Test
    @Transactional
    public void getAllAuditProfiles() throws Exception {
        // Initialize the database
        auditProfileRepository.saveAndFlush(auditProfile);

        // Get all the auditProfiles
        restAuditProfileMockMvc.perform(get("/api/audit-profiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auditProfile.getId().intValue())))
                .andExpect(jsonPath("$.[*].fiscalYear").value(hasItem(DEFAULT_FISCAL_YEAR.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getAuditProfile() throws Exception {
        // Initialize the database
        auditProfileRepository.saveAndFlush(auditProfile);

        // Get the auditProfile
        restAuditProfileMockMvc.perform(get("/api/audit-profiles/{id}", auditProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auditProfile.getId().intValue()))
            .andExpect(jsonPath("$.fiscalYear").value(DEFAULT_FISCAL_YEAR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuditProfile() throws Exception {
        // Get the auditProfile
        restAuditProfileMockMvc.perform(get("/api/audit-profiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditProfile() throws Exception {
        // Initialize the database
        auditProfileRepository.saveAndFlush(auditProfile);
        auditProfileSearchRepository.save(auditProfile);
        int databaseSizeBeforeUpdate = auditProfileRepository.findAll().size();

        // Update the auditProfile
        AuditProfile updatedAuditProfile = auditProfileRepository.findOne(auditProfile.getId());
        updatedAuditProfile.setFiscalYear(UPDATED_FISCAL_YEAR);
        updatedAuditProfile.setDescription(UPDATED_DESCRIPTION);
        updatedAuditProfile.setStatus(UPDATED_STATUS);

        restAuditProfileMockMvc.perform(put("/api/audit-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuditProfile)))
                .andExpect(status().isOk());

        // Validate the AuditProfile in the database
        List<AuditProfile> auditProfiles = auditProfileRepository.findAll();
        assertThat(auditProfiles).hasSize(databaseSizeBeforeUpdate);
        AuditProfile testAuditProfile = auditProfiles.get(auditProfiles.size() - 1);
        assertThat(testAuditProfile.getFiscalYear()).isEqualTo(UPDATED_FISCAL_YEAR);
        assertThat(testAuditProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAuditProfile.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the AuditProfile in ElasticSearch
        AuditProfile auditProfileEs = auditProfileSearchRepository.findOne(testAuditProfile.getId());
        assertThat(auditProfileEs).isEqualToComparingFieldByField(testAuditProfile);
    }

    @Test
    @Transactional
    public void deleteAuditProfile() throws Exception {
        // Initialize the database
        auditProfileRepository.saveAndFlush(auditProfile);
        auditProfileSearchRepository.save(auditProfile);
        int databaseSizeBeforeDelete = auditProfileRepository.findAll().size();

        // Get the auditProfile
        restAuditProfileMockMvc.perform(delete("/api/audit-profiles/{id}", auditProfile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean auditProfileExistsInEs = auditProfileSearchRepository.exists(auditProfile.getId());
        assertThat(auditProfileExistsInEs).isFalse();

        // Validate the database is empty
        List<AuditProfile> auditProfiles = auditProfileRepository.findAll();
        assertThat(auditProfiles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAuditProfile() throws Exception {
        // Initialize the database
        auditProfileRepository.saveAndFlush(auditProfile);
        auditProfileSearchRepository.save(auditProfile);

        // Search the auditProfile
        restAuditProfileMockMvc.perform(get("/api/_search/audit-profiles?query=id:" + auditProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYear").value(hasItem(DEFAULT_FISCAL_YEAR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
