package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.GlobalConfiguration;
import com.pwc.assurance.adc.repository.GlobalConfigurationRepository;
import com.pwc.assurance.adc.repository.search.GlobalConfigurationSearchRepository;

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
 * Test class for the GlobalConfigurationResource REST controller.
 *
 * @see GlobalConfigurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class GlobalConfigurationResourceIntTest {
    private static final String DEFAULT_PROPERTY_KEY = "AAAAA";
    private static final String UPDATED_PROPERTY_KEY = "BBBBB";
    private static final String DEFAULT_PROPERTY_VALUE = "AAAAA";
    private static final String UPDATED_PROPERTY_VALUE = "BBBBB";

    @Inject
    private GlobalConfigurationRepository globalConfigurationRepository;

    @Inject
    private GlobalConfigurationSearchRepository globalConfigurationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGlobalConfigurationMockMvc;

    private GlobalConfiguration globalConfiguration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GlobalConfigurationResource globalConfigurationResource = new GlobalConfigurationResource();
        ReflectionTestUtils.setField(globalConfigurationResource, "globalConfigurationSearchRepository", globalConfigurationSearchRepository);
        ReflectionTestUtils.setField(globalConfigurationResource, "globalConfigurationRepository", globalConfigurationRepository);
        this.restGlobalConfigurationMockMvc = MockMvcBuilders.standaloneSetup(globalConfigurationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalConfiguration createEntity(EntityManager em) {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration = new GlobalConfiguration()
                .propertyKey(DEFAULT_PROPERTY_KEY)
                .propertyValue(DEFAULT_PROPERTY_VALUE);
        return globalConfiguration;
    }

    @Before
    public void initTest() {
        globalConfigurationSearchRepository.deleteAll();
        globalConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createGlobalConfiguration() throws Exception {
        int databaseSizeBeforeCreate = globalConfigurationRepository.findAll().size();

        // Create the GlobalConfiguration

        restGlobalConfigurationMockMvc.perform(post("/api/global-configurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(globalConfiguration)))
                .andExpect(status().isCreated());

        // Validate the GlobalConfiguration in the database
        List<GlobalConfiguration> globalConfigurations = globalConfigurationRepository.findAll();
        assertThat(globalConfigurations).hasSize(databaseSizeBeforeCreate + 1);
        GlobalConfiguration testGlobalConfiguration = globalConfigurations.get(globalConfigurations.size() - 1);
        assertThat(testGlobalConfiguration.getPropertyKey()).isEqualTo(DEFAULT_PROPERTY_KEY);
        assertThat(testGlobalConfiguration.getPropertyValue()).isEqualTo(DEFAULT_PROPERTY_VALUE);

        // Validate the GlobalConfiguration in ElasticSearch
        GlobalConfiguration globalConfigurationEs = globalConfigurationSearchRepository.findOne(testGlobalConfiguration.getId());
        assertThat(globalConfigurationEs).isEqualToComparingFieldByField(testGlobalConfiguration);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigurations() throws Exception {
        // Initialize the database
        globalConfigurationRepository.saveAndFlush(globalConfiguration);

        // Get all the globalConfigurations
        restGlobalConfigurationMockMvc.perform(get("/api/global-configurations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(globalConfiguration.getId().intValue())))
                .andExpect(jsonPath("$.[*].propertyKey").value(hasItem(DEFAULT_PROPERTY_KEY.toString())))
                .andExpect(jsonPath("$.[*].propertyValue").value(hasItem(DEFAULT_PROPERTY_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getGlobalConfiguration() throws Exception {
        // Initialize the database
        globalConfigurationRepository.saveAndFlush(globalConfiguration);

        // Get the globalConfiguration
        restGlobalConfigurationMockMvc.perform(get("/api/global-configurations/{id}", globalConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(globalConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.propertyKey").value(DEFAULT_PROPERTY_KEY.toString()))
            .andExpect(jsonPath("$.propertyValue").value(DEFAULT_PROPERTY_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGlobalConfiguration() throws Exception {
        // Get the globalConfiguration
        restGlobalConfigurationMockMvc.perform(get("/api/global-configurations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGlobalConfiguration() throws Exception {
        // Initialize the database
        globalConfigurationRepository.saveAndFlush(globalConfiguration);
        globalConfigurationSearchRepository.save(globalConfiguration);
        int databaseSizeBeforeUpdate = globalConfigurationRepository.findAll().size();

        // Update the globalConfiguration
        GlobalConfiguration updatedGlobalConfiguration = globalConfigurationRepository.findOne(globalConfiguration.getId());
        updatedGlobalConfiguration
                .propertyKey(UPDATED_PROPERTY_KEY)
                .propertyValue(UPDATED_PROPERTY_VALUE);

        restGlobalConfigurationMockMvc.perform(put("/api/global-configurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGlobalConfiguration)))
                .andExpect(status().isOk());

        // Validate the GlobalConfiguration in the database
        List<GlobalConfiguration> globalConfigurations = globalConfigurationRepository.findAll();
        assertThat(globalConfigurations).hasSize(databaseSizeBeforeUpdate);
        GlobalConfiguration testGlobalConfiguration = globalConfigurations.get(globalConfigurations.size() - 1);
        assertThat(testGlobalConfiguration.getPropertyKey()).isEqualTo(UPDATED_PROPERTY_KEY);
        assertThat(testGlobalConfiguration.getPropertyValue()).isEqualTo(UPDATED_PROPERTY_VALUE);

        // Validate the GlobalConfiguration in ElasticSearch
        GlobalConfiguration globalConfigurationEs = globalConfigurationSearchRepository.findOne(testGlobalConfiguration.getId());
        assertThat(globalConfigurationEs).isEqualToComparingFieldByField(testGlobalConfiguration);
    }

    @Test
    @Transactional
    public void deleteGlobalConfiguration() throws Exception {
        // Initialize the database
        globalConfigurationRepository.saveAndFlush(globalConfiguration);
        globalConfigurationSearchRepository.save(globalConfiguration);
        int databaseSizeBeforeDelete = globalConfigurationRepository.findAll().size();

        // Get the globalConfiguration
        restGlobalConfigurationMockMvc.perform(delete("/api/global-configurations/{id}", globalConfiguration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean globalConfigurationExistsInEs = globalConfigurationSearchRepository.exists(globalConfiguration.getId());
        assertThat(globalConfigurationExistsInEs).isFalse();

        // Validate the database is empty
        List<GlobalConfiguration> globalConfigurations = globalConfigurationRepository.findAll();
        assertThat(globalConfigurations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGlobalConfiguration() throws Exception {
        // Initialize the database
        globalConfigurationRepository.saveAndFlush(globalConfiguration);
        globalConfigurationSearchRepository.save(globalConfiguration);

        // Search the globalConfiguration
        restGlobalConfigurationMockMvc.perform(get("/api/_search/global-configurations?query=id:" + globalConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].propertyKey").value(hasItem(DEFAULT_PROPERTY_KEY.toString())))
            .andExpect(jsonPath("$.[*].propertyValue").value(hasItem(DEFAULT_PROPERTY_VALUE.toString())));
    }
}
