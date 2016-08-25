package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.LicenseType;
import com.pwc.assurance.adc.repository.LicenseTypeRepository;
import com.pwc.assurance.adc.repository.search.LicenseTypeSearchRepository;

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
 * Test class for the LicenseTypeResource REST controller.
 *
 * @see LicenseTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class LicenseTypeResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private LicenseTypeRepository licenseTypeRepository;

    @Inject
    private LicenseTypeSearchRepository licenseTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLicenseTypeMockMvc;

    private LicenseType licenseType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LicenseTypeResource licenseTypeResource = new LicenseTypeResource();
        ReflectionTestUtils.setField(licenseTypeResource, "licenseTypeSearchRepository", licenseTypeSearchRepository);
        ReflectionTestUtils.setField(licenseTypeResource, "licenseTypeRepository", licenseTypeRepository);
        this.restLicenseTypeMockMvc = MockMvcBuilders.standaloneSetup(licenseTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LicenseType createEntity(EntityManager em) {
        LicenseType licenseType = new LicenseType();
        licenseType = new LicenseType();
        licenseType.setCode(DEFAULT_CODE);
        licenseType.setName(DEFAULT_NAME);
        licenseType.setDescription(DEFAULT_DESCRIPTION);
        return licenseType;
    }

    @Before
    public void initTest() {
        licenseTypeSearchRepository.deleteAll();
        licenseType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicenseType() throws Exception {
        int databaseSizeBeforeCreate = licenseTypeRepository.findAll().size();

        // Create the LicenseType

        restLicenseTypeMockMvc.perform(post("/api/license-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(licenseType)))
                .andExpect(status().isCreated());

        // Validate the LicenseType in the database
        List<LicenseType> licenseTypes = licenseTypeRepository.findAll();
        assertThat(licenseTypes).hasSize(databaseSizeBeforeCreate + 1);
        LicenseType testLicenseType = licenseTypes.get(licenseTypes.size() - 1);
        assertThat(testLicenseType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLicenseType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLicenseType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the LicenseType in ElasticSearch
        LicenseType licenseTypeEs = licenseTypeSearchRepository.findOne(testLicenseType.getId());
        assertThat(licenseTypeEs).isEqualToComparingFieldByField(testLicenseType);
    }

    @Test
    @Transactional
    public void getAllLicenseTypes() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get all the licenseTypes
        restLicenseTypeMockMvc.perform(get("/api/license-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(licenseType.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLicenseType() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);

        // Get the licenseType
        restLicenseTypeMockMvc.perform(get("/api/license-types/{id}", licenseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(licenseType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLicenseType() throws Exception {
        // Get the licenseType
        restLicenseTypeMockMvc.perform(get("/api/license-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicenseType() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);
        licenseTypeSearchRepository.save(licenseType);
        int databaseSizeBeforeUpdate = licenseTypeRepository.findAll().size();

        // Update the licenseType
        LicenseType updatedLicenseType = licenseTypeRepository.findOne(licenseType.getId());
        updatedLicenseType.setCode(UPDATED_CODE);
        updatedLicenseType.setName(UPDATED_NAME);
        updatedLicenseType.setDescription(UPDATED_DESCRIPTION);

        restLicenseTypeMockMvc.perform(put("/api/license-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLicenseType)))
                .andExpect(status().isOk());

        // Validate the LicenseType in the database
        List<LicenseType> licenseTypes = licenseTypeRepository.findAll();
        assertThat(licenseTypes).hasSize(databaseSizeBeforeUpdate);
        LicenseType testLicenseType = licenseTypes.get(licenseTypes.size() - 1);
        assertThat(testLicenseType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLicenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLicenseType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the LicenseType in ElasticSearch
        LicenseType licenseTypeEs = licenseTypeSearchRepository.findOne(testLicenseType.getId());
        assertThat(licenseTypeEs).isEqualToComparingFieldByField(testLicenseType);
    }

    @Test
    @Transactional
    public void deleteLicenseType() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);
        licenseTypeSearchRepository.save(licenseType);
        int databaseSizeBeforeDelete = licenseTypeRepository.findAll().size();

        // Get the licenseType
        restLicenseTypeMockMvc.perform(delete("/api/license-types/{id}", licenseType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean licenseTypeExistsInEs = licenseTypeSearchRepository.exists(licenseType.getId());
        assertThat(licenseTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<LicenseType> licenseTypes = licenseTypeRepository.findAll();
        assertThat(licenseTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLicenseType() throws Exception {
        // Initialize the database
        licenseTypeRepository.saveAndFlush(licenseType);
        licenseTypeSearchRepository.save(licenseType);

        // Search the licenseType
        restLicenseTypeMockMvc.perform(get("/api/_search/license-types?query=id:" + licenseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
