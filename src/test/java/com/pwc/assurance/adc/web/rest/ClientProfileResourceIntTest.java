package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ClientProfile;
import com.pwc.assurance.adc.repository.ClientProfileRepository;
import com.pwc.assurance.adc.repository.search.ClientProfileSearchRepository;

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
 * Test class for the ClientProfileResource REST controller.
 *
 * @see ClientProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ClientProfileResourceIntTest {

    @Inject
    private ClientProfileRepository clientProfileRepository;

    @Inject
    private ClientProfileSearchRepository clientProfileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClientProfileMockMvc;

    private ClientProfile clientProfile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientProfileResource clientProfileResource = new ClientProfileResource();
        ReflectionTestUtils.setField(clientProfileResource, "clientProfileSearchRepository", clientProfileSearchRepository);
        ReflectionTestUtils.setField(clientProfileResource, "clientProfileRepository", clientProfileRepository);
        this.restClientProfileMockMvc = MockMvcBuilders.standaloneSetup(clientProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientProfile createEntity(EntityManager em) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile = new ClientProfile();
        return clientProfile;
    }

    @Before
    public void initTest() {
        clientProfileSearchRepository.deleteAll();
        clientProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientProfile() throws Exception {
        int databaseSizeBeforeCreate = clientProfileRepository.findAll().size();

        // Create the ClientProfile

        restClientProfileMockMvc.perform(post("/api/client-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientProfile)))
                .andExpect(status().isCreated());

        // Validate the ClientProfile in the database
        List<ClientProfile> clientProfiles = clientProfileRepository.findAll();
        assertThat(clientProfiles).hasSize(databaseSizeBeforeCreate + 1);
        ClientProfile testClientProfile = clientProfiles.get(clientProfiles.size() - 1);

        // Validate the ClientProfile in ElasticSearch
        ClientProfile clientProfileEs = clientProfileSearchRepository.findOne(testClientProfile.getId());
        assertThat(clientProfileEs).isEqualToComparingFieldByField(testClientProfile);
    }

    @Test
    @Transactional
    public void getAllClientProfiles() throws Exception {
        // Initialize the database
        clientProfileRepository.saveAndFlush(clientProfile);

        // Get all the clientProfiles
        restClientProfileMockMvc.perform(get("/api/client-profiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientProfile.getId().intValue())));
    }

    @Test
    @Transactional
    public void getClientProfile() throws Exception {
        // Initialize the database
        clientProfileRepository.saveAndFlush(clientProfile);

        // Get the clientProfile
        restClientProfileMockMvc.perform(get("/api/client-profiles/{id}", clientProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientProfile.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClientProfile() throws Exception {
        // Get the clientProfile
        restClientProfileMockMvc.perform(get("/api/client-profiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientProfile() throws Exception {
        // Initialize the database
        clientProfileRepository.saveAndFlush(clientProfile);
        clientProfileSearchRepository.save(clientProfile);
        int databaseSizeBeforeUpdate = clientProfileRepository.findAll().size();

        // Update the clientProfile
        ClientProfile updatedClientProfile = clientProfileRepository.findOne(clientProfile.getId());

        restClientProfileMockMvc.perform(put("/api/client-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClientProfile)))
                .andExpect(status().isOk());

        // Validate the ClientProfile in the database
        List<ClientProfile> clientProfiles = clientProfileRepository.findAll();
        assertThat(clientProfiles).hasSize(databaseSizeBeforeUpdate);
        ClientProfile testClientProfile = clientProfiles.get(clientProfiles.size() - 1);

        // Validate the ClientProfile in ElasticSearch
        ClientProfile clientProfileEs = clientProfileSearchRepository.findOne(testClientProfile.getId());
        assertThat(clientProfileEs).isEqualToComparingFieldByField(testClientProfile);
    }

    @Test
    @Transactional
    public void deleteClientProfile() throws Exception {
        // Initialize the database
        clientProfileRepository.saveAndFlush(clientProfile);
        clientProfileSearchRepository.save(clientProfile);
        int databaseSizeBeforeDelete = clientProfileRepository.findAll().size();

        // Get the clientProfile
        restClientProfileMockMvc.perform(delete("/api/client-profiles/{id}", clientProfile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientProfileExistsInEs = clientProfileSearchRepository.exists(clientProfile.getId());
        assertThat(clientProfileExistsInEs).isFalse();

        // Validate the database is empty
        List<ClientProfile> clientProfiles = clientProfileRepository.findAll();
        assertThat(clientProfiles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClientProfile() throws Exception {
        // Initialize the database
        clientProfileRepository.saveAndFlush(clientProfile);
        clientProfileSearchRepository.save(clientProfile);

        // Search the clientProfile
        restClientProfileMockMvc.perform(get("/api/_search/client-profiles?query=id:" + clientProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientProfile.getId().intValue())));
    }
}
