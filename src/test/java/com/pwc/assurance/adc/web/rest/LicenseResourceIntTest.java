package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.License;
import com.pwc.assurance.adc.repository.LicenseRepository;
import com.pwc.assurance.adc.repository.search.LicenseSearchRepository;

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
 * Test class for the LicenseResource REST controller.
 *
 * @see LicenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class LicenseResourceIntTest {
    private static final String DEFAULT_CONTACT_NAME = "AAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBB";
    private static final String DEFAULT_CONTACT_EMAIL = "AAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBB";
    private static final String DEFAULT_ACTIVATION_TOKEN = "AAAAA";
    private static final String UPDATED_ACTIVATION_TOKEN = "BBBBB";

    @Inject
    private LicenseRepository licenseRepository;

    @Inject
    private LicenseSearchRepository licenseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLicenseMockMvc;

    private License license;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LicenseResource licenseResource = new LicenseResource();
        ReflectionTestUtils.setField(licenseResource, "licenseSearchRepository", licenseSearchRepository);
        ReflectionTestUtils.setField(licenseResource, "licenseRepository", licenseRepository);
        this.restLicenseMockMvc = MockMvcBuilders.standaloneSetup(licenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createEntity(EntityManager em) {
        License license = new License();
        license = new License()
                .contactName(DEFAULT_CONTACT_NAME)
                .contactEmail(DEFAULT_CONTACT_EMAIL)
                .activationToken(DEFAULT_ACTIVATION_TOKEN);
        return license;
    }

    @Before
    public void initTest() {
        licenseSearchRepository.deleteAll();
        license = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicense() throws Exception {
        int databaseSizeBeforeCreate = licenseRepository.findAll().size();

        // Create the License

        restLicenseMockMvc.perform(post("/api/licenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(license)))
                .andExpect(status().isCreated());

        // Validate the License in the database
        List<License> licenses = licenseRepository.findAll();
        assertThat(licenses).hasSize(databaseSizeBeforeCreate + 1);
        License testLicense = licenses.get(licenses.size() - 1);
        assertThat(testLicense.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testLicense.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testLicense.getActivationToken()).isEqualTo(DEFAULT_ACTIVATION_TOKEN);

        // Validate the License in ElasticSearch
        License licenseEs = licenseSearchRepository.findOne(testLicense.getId());
        assertThat(licenseEs).isEqualToComparingFieldByField(testLicense);
    }

    @Test
    @Transactional
    public void getAllLicenses() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenses
        restLicenseMockMvc.perform(get("/api/licenses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(license.getId().intValue())))
                .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
                .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].activationToken").value(hasItem(DEFAULT_ACTIVATION_TOKEN.toString())));
    }

    @Test
    @Transactional
    public void getLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get the license
        restLicenseMockMvc.perform(get("/api/licenses/{id}", license.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(license.getId().intValue()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.activationToken").value(DEFAULT_ACTIVATION_TOKEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLicense() throws Exception {
        // Get the license
        restLicenseMockMvc.perform(get("/api/licenses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);
        licenseSearchRepository.save(license);
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();

        // Update the license
        License updatedLicense = licenseRepository.findOne(license.getId());
        updatedLicense
                .contactName(UPDATED_CONTACT_NAME)
                .contactEmail(UPDATED_CONTACT_EMAIL)
                .activationToken(UPDATED_ACTIVATION_TOKEN);

        restLicenseMockMvc.perform(put("/api/licenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLicense)))
                .andExpect(status().isOk());

        // Validate the License in the database
        List<License> licenses = licenseRepository.findAll();
        assertThat(licenses).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenses.get(licenses.size() - 1);
        assertThat(testLicense.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testLicense.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testLicense.getActivationToken()).isEqualTo(UPDATED_ACTIVATION_TOKEN);

        // Validate the License in ElasticSearch
        License licenseEs = licenseSearchRepository.findOne(testLicense.getId());
        assertThat(licenseEs).isEqualToComparingFieldByField(testLicense);
    }

    @Test
    @Transactional
    public void deleteLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);
        licenseSearchRepository.save(license);
        int databaseSizeBeforeDelete = licenseRepository.findAll().size();

        // Get the license
        restLicenseMockMvc.perform(delete("/api/licenses/{id}", license.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean licenseExistsInEs = licenseSearchRepository.exists(license.getId());
        assertThat(licenseExistsInEs).isFalse();

        // Validate the database is empty
        List<License> licenses = licenseRepository.findAll();
        assertThat(licenses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);
        licenseSearchRepository.save(license);

        // Search the license
        restLicenseMockMvc.perform(get("/api/_search/licenses?query=id:" + license.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(license.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].activationToken").value(hasItem(DEFAULT_ACTIVATION_TOKEN.toString())));
    }
}
