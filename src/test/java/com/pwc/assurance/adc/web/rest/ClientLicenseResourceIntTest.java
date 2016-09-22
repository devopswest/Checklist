package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ClientLicense;
import com.pwc.assurance.adc.repository.ClientLicenseRepository;
import com.pwc.assurance.adc.service.ClientLicenseService;
import com.pwc.assurance.adc.repository.search.ClientLicenseSearchRepository;
import com.pwc.assurance.adc.service.dto.ClientLicenseDTO;
import com.pwc.assurance.adc.service.mapper.ClientLicenseMapper;

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

/**
 * Test class for the ClientLicenseResource REST controller.
 *
 * @see ClientLicenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ClientLicenseResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_CONTACT_NAME = "AAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBB";
    private static final String DEFAULT_CONTACT_EMAIL = "AAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EXPIRATION_DATE_STR = dateTimeFormatter.format(DEFAULT_EXPIRATION_DATE);
    private static final String DEFAULT_ACTIVATION_TOKEN = "A";
    private static final String UPDATED_ACTIVATION_TOKEN = "B";

    @Inject
    private ClientLicenseRepository clientLicenseRepository;

    @Inject
    private ClientLicenseMapper clientLicenseMapper;

    @Inject
    private ClientLicenseService clientLicenseService;

    @Inject
    private ClientLicenseSearchRepository clientLicenseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClientLicenseMockMvc;

    private ClientLicense clientLicense;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientLicenseResource clientLicenseResource = new ClientLicenseResource();
        ReflectionTestUtils.setField(clientLicenseResource, "clientLicenseService", clientLicenseService);
        this.restClientLicenseMockMvc = MockMvcBuilders.standaloneSetup(clientLicenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientLicense createEntity(EntityManager em) {
        ClientLicense clientLicense = new ClientLicense();
        clientLicense = new ClientLicense()
                .contactName(DEFAULT_CONTACT_NAME)
                .contactEmail(DEFAULT_CONTACT_EMAIL)
                .expirationDate(DEFAULT_EXPIRATION_DATE)
                .activationToken(DEFAULT_ACTIVATION_TOKEN);
        return clientLicense;
    }

    @Before
    public void initTest() {
        clientLicenseSearchRepository.deleteAll();
        clientLicense = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientLicense() throws Exception {
        int databaseSizeBeforeCreate = clientLicenseRepository.findAll().size();

        // Create the ClientLicense
        ClientLicenseDTO clientLicenseDTO = clientLicenseMapper.clientLicenseToClientLicenseDTO(clientLicense);

        restClientLicenseMockMvc.perform(post("/api/client-licenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientLicenseDTO)))
                .andExpect(status().isCreated());

        // Validate the ClientLicense in the database
        List<ClientLicense> clientLicenses = clientLicenseRepository.findAll();
        assertThat(clientLicenses).hasSize(databaseSizeBeforeCreate + 1);
        ClientLicense testClientLicense = clientLicenses.get(clientLicenses.size() - 1);
        assertThat(testClientLicense.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testClientLicense.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testClientLicense.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testClientLicense.getActivationToken()).isEqualTo(DEFAULT_ACTIVATION_TOKEN);

        // Validate the ClientLicense in ElasticSearch
        ClientLicense clientLicenseEs = clientLicenseSearchRepository.findOne(testClientLicense.getId());
        assertThat(clientLicenseEs).isEqualToComparingFieldByField(testClientLicense);
    }

    @Test
    @Transactional
    public void getAllClientLicenses() throws Exception {
        // Initialize the database
        clientLicenseRepository.saveAndFlush(clientLicense);

        // Get all the clientLicenses
        restClientLicenseMockMvc.perform(get("/api/client-licenses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientLicense.getId().intValue())))
                .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
                .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].activationToken").value(hasItem(DEFAULT_ACTIVATION_TOKEN.toString())));
    }

    @Test
    @Transactional
    public void getClientLicense() throws Exception {
        // Initialize the database
        clientLicenseRepository.saveAndFlush(clientLicense);

        // Get the clientLicense
        restClientLicenseMockMvc.perform(get("/api/client-licenses/{id}", clientLicense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientLicense.getId().intValue()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE_STR))
            .andExpect(jsonPath("$.activationToken").value(DEFAULT_ACTIVATION_TOKEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClientLicense() throws Exception {
        // Get the clientLicense
        restClientLicenseMockMvc.perform(get("/api/client-licenses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientLicense() throws Exception {
        // Initialize the database
        clientLicenseRepository.saveAndFlush(clientLicense);
        clientLicenseSearchRepository.save(clientLicense);
        int databaseSizeBeforeUpdate = clientLicenseRepository.findAll().size();

        // Update the clientLicense
        ClientLicense updatedClientLicense = clientLicenseRepository.findOne(clientLicense.getId());
        updatedClientLicense
                .contactName(UPDATED_CONTACT_NAME)
                .contactEmail(UPDATED_CONTACT_EMAIL)
                .expirationDate(UPDATED_EXPIRATION_DATE)
                .activationToken(UPDATED_ACTIVATION_TOKEN);
        ClientLicenseDTO clientLicenseDTO = clientLicenseMapper.clientLicenseToClientLicenseDTO(updatedClientLicense);

        restClientLicenseMockMvc.perform(put("/api/client-licenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientLicenseDTO)))
                .andExpect(status().isOk());

        // Validate the ClientLicense in the database
        List<ClientLicense> clientLicenses = clientLicenseRepository.findAll();
        assertThat(clientLicenses).hasSize(databaseSizeBeforeUpdate);
        ClientLicense testClientLicense = clientLicenses.get(clientLicenses.size() - 1);
        assertThat(testClientLicense.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testClientLicense.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testClientLicense.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testClientLicense.getActivationToken()).isEqualTo(UPDATED_ACTIVATION_TOKEN);

        // Validate the ClientLicense in ElasticSearch
        ClientLicense clientLicenseEs = clientLicenseSearchRepository.findOne(testClientLicense.getId());
        assertThat(clientLicenseEs).isEqualToComparingFieldByField(testClientLicense);
    }

    @Test
    @Transactional
    public void deleteClientLicense() throws Exception {
        // Initialize the database
        clientLicenseRepository.saveAndFlush(clientLicense);
        clientLicenseSearchRepository.save(clientLicense);
        int databaseSizeBeforeDelete = clientLicenseRepository.findAll().size();

        // Get the clientLicense
        restClientLicenseMockMvc.perform(delete("/api/client-licenses/{id}", clientLicense.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientLicenseExistsInEs = clientLicenseSearchRepository.exists(clientLicense.getId());
        assertThat(clientLicenseExistsInEs).isFalse();

        // Validate the database is empty
        List<ClientLicense> clientLicenses = clientLicenseRepository.findAll();
        assertThat(clientLicenses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClientLicense() throws Exception {
        // Initialize the database
        clientLicenseRepository.saveAndFlush(clientLicense);
        clientLicenseSearchRepository.save(clientLicense);

        // Search the clientLicense
        restClientLicenseMockMvc.perform(get("/api/_search/client-licenses?query=id:" + clientLicense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientLicense.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].activationToken").value(hasItem(DEFAULT_ACTIVATION_TOKEN.toString())));
    }
}
