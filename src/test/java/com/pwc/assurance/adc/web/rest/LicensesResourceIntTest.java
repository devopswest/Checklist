package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Licenses;
import com.pwc.assurance.adc.repository.LicensesRepository;
import com.pwc.assurance.adc.repository.search.LicensesSearchRepository;

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
 * Test class for the LicensesResource REST controller.
 *
 * @see LicensesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class LicensesResourceIntTest {
    private static final String DEFAULT_CONTACT_NAME = "AAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBB";
    private static final String DEFAULT_CONTACT_EMAIL = "AAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBB";
    private static final String DEFAULT_ACTIVATION_TOKEN = "AAAAA";
    private static final String UPDATED_ACTIVATION_TOKEN = "BBBBB";

    @Inject
    private LicensesRepository licensesRepository;

    @Inject
    private LicensesSearchRepository licensesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLicensesMockMvc;

    private Licenses licenses;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LicensesResource licensesResource = new LicensesResource();
        ReflectionTestUtils.setField(licensesResource, "licensesSearchRepository", licensesSearchRepository);
        ReflectionTestUtils.setField(licensesResource, "licensesRepository", licensesRepository);
        this.restLicensesMockMvc = MockMvcBuilders.standaloneSetup(licensesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Licenses createEntity(EntityManager em) {
        Licenses licenses = new Licenses();
        licenses = new Licenses();
        licenses.setContactName(DEFAULT_CONTACT_NAME);
        licenses.setContactEmail(DEFAULT_CONTACT_EMAIL);
        licenses.setActivationToken(DEFAULT_ACTIVATION_TOKEN);
        return licenses;
    }

    @Before
    public void initTest() {
        licensesSearchRepository.deleteAll();
        licenses = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicenses() throws Exception {
        int databaseSizeBeforeCreate = licensesRepository.findAll().size();

        // Create the Licenses

        restLicensesMockMvc.perform(post("/api/licenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(licenses)))
                .andExpect(status().isCreated());

        // Validate the Licenses in the database
        List<Licenses> licenses = licensesRepository.findAll();
        assertThat(licenses).hasSize(databaseSizeBeforeCreate + 1);
        Licenses testLicenses = licenses.get(licenses.size() - 1);
        assertThat(testLicenses.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testLicenses.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testLicenses.getActivationToken()).isEqualTo(DEFAULT_ACTIVATION_TOKEN);

        // Validate the Licenses in ElasticSearch
        Licenses licensesEs = licensesSearchRepository.findOne(testLicenses.getId());
        assertThat(licensesEs).isEqualToComparingFieldByField(testLicenses);
    }

    @Test
    @Transactional
    public void getAllLicenses() throws Exception {
        // Initialize the database
        licensesRepository.saveAndFlush(licenses);

        // Get all the licenses
        restLicensesMockMvc.perform(get("/api/licenses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(licenses.getId().intValue())))
                .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
                .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].activationToken").value(hasItem(DEFAULT_ACTIVATION_TOKEN.toString())));
    }

    @Test
    @Transactional
    public void getLicenses() throws Exception {
        // Initialize the database
        licensesRepository.saveAndFlush(licenses);

        // Get the licenses
        restLicensesMockMvc.perform(get("/api/licenses/{id}", licenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(licenses.getId().intValue()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.activationToken").value(DEFAULT_ACTIVATION_TOKEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLicenses() throws Exception {
        // Get the licenses
        restLicensesMockMvc.perform(get("/api/licenses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicenses() throws Exception {
        // Initialize the database
        licensesRepository.saveAndFlush(licenses);
        licensesSearchRepository.save(licenses);
        int databaseSizeBeforeUpdate = licensesRepository.findAll().size();

        // Update the licenses
        Licenses updatedLicenses = licensesRepository.findOne(licenses.getId());
        updatedLicenses.setContactName(UPDATED_CONTACT_NAME);
        updatedLicenses.setContactEmail(UPDATED_CONTACT_EMAIL);
        updatedLicenses.setActivationToken(UPDATED_ACTIVATION_TOKEN);

        restLicensesMockMvc.perform(put("/api/licenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLicenses)))
                .andExpect(status().isOk());

        // Validate the Licenses in the database
        List<Licenses> licenses = licensesRepository.findAll();
        assertThat(licenses).hasSize(databaseSizeBeforeUpdate);
        Licenses testLicenses = licenses.get(licenses.size() - 1);
        assertThat(testLicenses.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testLicenses.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testLicenses.getActivationToken()).isEqualTo(UPDATED_ACTIVATION_TOKEN);

        // Validate the Licenses in ElasticSearch
        Licenses licensesEs = licensesSearchRepository.findOne(testLicenses.getId());
        assertThat(licensesEs).isEqualToComparingFieldByField(testLicenses);
    }

    @Test
    @Transactional
    public void deleteLicenses() throws Exception {
        // Initialize the database
        licensesRepository.saveAndFlush(licenses);
        licensesSearchRepository.save(licenses);
        int databaseSizeBeforeDelete = licensesRepository.findAll().size();

        // Get the licenses
        restLicensesMockMvc.perform(delete("/api/licenses/{id}", licenses.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean licensesExistsInEs = licensesSearchRepository.exists(licenses.getId());
        assertThat(licensesExistsInEs).isFalse();

        // Validate the database is empty
        List<Licenses> licenses = licensesRepository.findAll();
        assertThat(licenses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLicenses() throws Exception {
        // Initialize the database
        licensesRepository.saveAndFlush(licenses);
        licensesSearchRepository.save(licenses);

        // Search the licenses
        restLicensesMockMvc.perform(get("/api/_search/licenses?query=id:" + licenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].activationToken").value(hasItem(DEFAULT_ACTIVATION_TOKEN.toString())));
    }
}
