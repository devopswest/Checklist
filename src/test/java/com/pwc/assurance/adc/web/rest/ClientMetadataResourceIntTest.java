package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ClientMetadata;
import com.pwc.assurance.adc.repository.ClientMetadataRepository;
import com.pwc.assurance.adc.repository.search.ClientMetadataSearchRepository;

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
 * Test class for the ClientMetadataResource REST controller.
 *
 * @see ClientMetadataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ClientMetadataResourceIntTest {
    private static final String DEFAULT_METADATA_VALUE = "AAAAA";
    private static final String UPDATED_METADATA_VALUE = "BBBBB";

    @Inject
    private ClientMetadataRepository clientMetadataRepository;

    @Inject
    private ClientMetadataSearchRepository clientMetadataSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClientMetadataMockMvc;

    private ClientMetadata clientMetadata;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientMetadataResource clientMetadataResource = new ClientMetadataResource();
        ReflectionTestUtils.setField(clientMetadataResource, "clientMetadataSearchRepository", clientMetadataSearchRepository);
        ReflectionTestUtils.setField(clientMetadataResource, "clientMetadataRepository", clientMetadataRepository);
        this.restClientMetadataMockMvc = MockMvcBuilders.standaloneSetup(clientMetadataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientMetadata createEntity(EntityManager em) {
        ClientMetadata clientMetadata = new ClientMetadata();
        clientMetadata = new ClientMetadata()
                .metadataValue(DEFAULT_METADATA_VALUE);
        return clientMetadata;
    }

    @Before
    public void initTest() {
        clientMetadataSearchRepository.deleteAll();
        clientMetadata = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientMetadata() throws Exception {
        int databaseSizeBeforeCreate = clientMetadataRepository.findAll().size();

        // Create the ClientMetadata

        restClientMetadataMockMvc.perform(post("/api/client-metadata")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientMetadata)))
                .andExpect(status().isCreated());

        // Validate the ClientMetadata in the database
        List<ClientMetadata> clientMetadata = clientMetadataRepository.findAll();
        assertThat(clientMetadata).hasSize(databaseSizeBeforeCreate + 1);
        ClientMetadata testClientMetadata = clientMetadata.get(clientMetadata.size() - 1);
        assertThat(testClientMetadata.getMetadataValue()).isEqualTo(DEFAULT_METADATA_VALUE);

        // Validate the ClientMetadata in ElasticSearch
        ClientMetadata clientMetadataEs = clientMetadataSearchRepository.findOne(testClientMetadata.getId());
        assertThat(clientMetadataEs).isEqualToComparingFieldByField(testClientMetadata);
    }

    @Test
    @Transactional
    public void getAllClientMetadata() throws Exception {
        // Initialize the database
        clientMetadataRepository.saveAndFlush(clientMetadata);

        // Get all the clientMetadata
        restClientMetadataMockMvc.perform(get("/api/client-metadata?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientMetadata.getId().intValue())))
                .andExpect(jsonPath("$.[*].metadataValue").value(hasItem(DEFAULT_METADATA_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getClientMetadata() throws Exception {
        // Initialize the database
        clientMetadataRepository.saveAndFlush(clientMetadata);

        // Get the clientMetadata
        restClientMetadataMockMvc.perform(get("/api/client-metadata/{id}", clientMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientMetadata.getId().intValue()))
            .andExpect(jsonPath("$.metadataValue").value(DEFAULT_METADATA_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClientMetadata() throws Exception {
        // Get the clientMetadata
        restClientMetadataMockMvc.perform(get("/api/client-metadata/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientMetadata() throws Exception {
        // Initialize the database
        clientMetadataRepository.saveAndFlush(clientMetadata);
        clientMetadataSearchRepository.save(clientMetadata);
        int databaseSizeBeforeUpdate = clientMetadataRepository.findAll().size();

        // Update the clientMetadata
        ClientMetadata updatedClientMetadata = clientMetadataRepository.findOne(clientMetadata.getId());
        updatedClientMetadata
                .metadataValue(UPDATED_METADATA_VALUE);

        restClientMetadataMockMvc.perform(put("/api/client-metadata")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClientMetadata)))
                .andExpect(status().isOk());

        // Validate the ClientMetadata in the database
        List<ClientMetadata> clientMetadata = clientMetadataRepository.findAll();
        assertThat(clientMetadata).hasSize(databaseSizeBeforeUpdate);
        ClientMetadata testClientMetadata = clientMetadata.get(clientMetadata.size() - 1);
        assertThat(testClientMetadata.getMetadataValue()).isEqualTo(UPDATED_METADATA_VALUE);

        // Validate the ClientMetadata in ElasticSearch
        ClientMetadata clientMetadataEs = clientMetadataSearchRepository.findOne(testClientMetadata.getId());
        assertThat(clientMetadataEs).isEqualToComparingFieldByField(testClientMetadata);
    }

    @Test
    @Transactional
    public void deleteClientMetadata() throws Exception {
        // Initialize the database
        clientMetadataRepository.saveAndFlush(clientMetadata);
        clientMetadataSearchRepository.save(clientMetadata);
        int databaseSizeBeforeDelete = clientMetadataRepository.findAll().size();

        // Get the clientMetadata
        restClientMetadataMockMvc.perform(delete("/api/client-metadata/{id}", clientMetadata.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientMetadataExistsInEs = clientMetadataSearchRepository.exists(clientMetadata.getId());
        assertThat(clientMetadataExistsInEs).isFalse();

        // Validate the database is empty
        List<ClientMetadata> clientMetadata = clientMetadataRepository.findAll();
        assertThat(clientMetadata).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClientMetadata() throws Exception {
        // Initialize the database
        clientMetadataRepository.saveAndFlush(clientMetadata);
        clientMetadataSearchRepository.save(clientMetadata);

        // Search the clientMetadata
        restClientMetadataMockMvc.perform(get("/api/_search/client-metadata?query=id:" + clientMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].metadataValue").value(hasItem(DEFAULT_METADATA_VALUE.toString())));
    }
}
