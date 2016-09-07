package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.FeatureAuthority;
import com.pwc.assurance.adc.repository.FeatureAuthorityRepository;
import com.pwc.assurance.adc.repository.search.FeatureAuthoritySearchRepository;

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

import com.pwc.assurance.adc.domain.enumeration.ApplicationAuthorities;
/**
 * Test class for the FeatureAuthorityResource REST controller.
 *
 * @see FeatureAuthorityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class FeatureAuthorityResourceIntTest {

    private static final ApplicationAuthorities DEFAULT_AUTHORITY = ApplicationAuthorities.ROLE_ADMIN;
    private static final ApplicationAuthorities UPDATED_AUTHORITY = ApplicationAuthorities.ROLE_USER;

    @Inject
    private FeatureAuthorityRepository featureAuthorityRepository;

    @Inject
    private FeatureAuthoritySearchRepository featureAuthoritySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFeatureAuthorityMockMvc;

    private FeatureAuthority featureAuthority;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeatureAuthorityResource featureAuthorityResource = new FeatureAuthorityResource();
        ReflectionTestUtils.setField(featureAuthorityResource, "featureAuthoritySearchRepository", featureAuthoritySearchRepository);
        ReflectionTestUtils.setField(featureAuthorityResource, "featureAuthorityRepository", featureAuthorityRepository);
        this.restFeatureAuthorityMockMvc = MockMvcBuilders.standaloneSetup(featureAuthorityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeatureAuthority createEntity(EntityManager em) {
        FeatureAuthority featureAuthority = new FeatureAuthority();
        featureAuthority = new FeatureAuthority()
                .authority(DEFAULT_AUTHORITY);
        return featureAuthority;
    }

    @Before
    public void initTest() {
        featureAuthoritySearchRepository.deleteAll();
        featureAuthority = createEntity(em);
    }

    @Test
    @Transactional
    public void createFeatureAuthority() throws Exception {
        int databaseSizeBeforeCreate = featureAuthorityRepository.findAll().size();

        // Create the FeatureAuthority

        restFeatureAuthorityMockMvc.perform(post("/api/feature-authorities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(featureAuthority)))
                .andExpect(status().isCreated());

        // Validate the FeatureAuthority in the database
        List<FeatureAuthority> featureAuthorities = featureAuthorityRepository.findAll();
        assertThat(featureAuthorities).hasSize(databaseSizeBeforeCreate + 1);
        FeatureAuthority testFeatureAuthority = featureAuthorities.get(featureAuthorities.size() - 1);
        assertThat(testFeatureAuthority.getAuthority()).isEqualTo(DEFAULT_AUTHORITY);

        // Validate the FeatureAuthority in ElasticSearch
        FeatureAuthority featureAuthorityEs = featureAuthoritySearchRepository.findOne(testFeatureAuthority.getId());
        assertThat(featureAuthorityEs).isEqualToComparingFieldByField(testFeatureAuthority);
    }

    @Test
    @Transactional
    public void getAllFeatureAuthorities() throws Exception {
        // Initialize the database
        featureAuthorityRepository.saveAndFlush(featureAuthority);

        // Get all the featureAuthorities
        restFeatureAuthorityMockMvc.perform(get("/api/feature-authorities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(featureAuthority.getId().intValue())))
                .andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));
    }

    @Test
    @Transactional
    public void getFeatureAuthority() throws Exception {
        // Initialize the database
        featureAuthorityRepository.saveAndFlush(featureAuthority);

        // Get the featureAuthority
        restFeatureAuthorityMockMvc.perform(get("/api/feature-authorities/{id}", featureAuthority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(featureAuthority.getId().intValue()))
            .andExpect(jsonPath("$.authority").value(DEFAULT_AUTHORITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFeatureAuthority() throws Exception {
        // Get the featureAuthority
        restFeatureAuthorityMockMvc.perform(get("/api/feature-authorities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeatureAuthority() throws Exception {
        // Initialize the database
        featureAuthorityRepository.saveAndFlush(featureAuthority);
        featureAuthoritySearchRepository.save(featureAuthority);
        int databaseSizeBeforeUpdate = featureAuthorityRepository.findAll().size();

        // Update the featureAuthority
        FeatureAuthority updatedFeatureAuthority = featureAuthorityRepository.findOne(featureAuthority.getId());
        updatedFeatureAuthority
                .authority(UPDATED_AUTHORITY);

        restFeatureAuthorityMockMvc.perform(put("/api/feature-authorities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFeatureAuthority)))
                .andExpect(status().isOk());

        // Validate the FeatureAuthority in the database
        List<FeatureAuthority> featureAuthorities = featureAuthorityRepository.findAll();
        assertThat(featureAuthorities).hasSize(databaseSizeBeforeUpdate);
        FeatureAuthority testFeatureAuthority = featureAuthorities.get(featureAuthorities.size() - 1);
        assertThat(testFeatureAuthority.getAuthority()).isEqualTo(UPDATED_AUTHORITY);

        // Validate the FeatureAuthority in ElasticSearch
        FeatureAuthority featureAuthorityEs = featureAuthoritySearchRepository.findOne(testFeatureAuthority.getId());
        assertThat(featureAuthorityEs).isEqualToComparingFieldByField(testFeatureAuthority);
    }

    @Test
    @Transactional
    public void deleteFeatureAuthority() throws Exception {
        // Initialize the database
        featureAuthorityRepository.saveAndFlush(featureAuthority);
        featureAuthoritySearchRepository.save(featureAuthority);
        int databaseSizeBeforeDelete = featureAuthorityRepository.findAll().size();

        // Get the featureAuthority
        restFeatureAuthorityMockMvc.perform(delete("/api/feature-authorities/{id}", featureAuthority.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean featureAuthorityExistsInEs = featureAuthoritySearchRepository.exists(featureAuthority.getId());
        assertThat(featureAuthorityExistsInEs).isFalse();

        // Validate the database is empty
        List<FeatureAuthority> featureAuthorities = featureAuthorityRepository.findAll();
        assertThat(featureAuthorities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFeatureAuthority() throws Exception {
        // Initialize the database
        featureAuthorityRepository.saveAndFlush(featureAuthority);
        featureAuthoritySearchRepository.save(featureAuthority);

        // Search the featureAuthority
        restFeatureAuthorityMockMvc.perform(get("/api/_search/feature-authorities?query=id:" + featureAuthority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(featureAuthority.getId().intValue())))
            .andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));
    }
}
