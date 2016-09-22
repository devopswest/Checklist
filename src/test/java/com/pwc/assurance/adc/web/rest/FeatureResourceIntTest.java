package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Feature;
import com.pwc.assurance.adc.repository.FeatureRepository;
import com.pwc.assurance.adc.service.FeatureService;
import com.pwc.assurance.adc.repository.search.FeatureSearchRepository;
import com.pwc.assurance.adc.service.dto.FeatureDTO;
import com.pwc.assurance.adc.service.mapper.FeatureMapper;

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
 * Test class for the FeatureResource REST controller.
 *
 * @see FeatureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class FeatureResourceIntTest {
    private static final String DEFAULT_CODE = "A";
    private static final String UPDATED_CODE = "B";
    private static final String DEFAULT_LABEL = "A";
    private static final String UPDATED_LABEL = "B";

    @Inject
    private FeatureRepository featureRepository;

    @Inject
    private FeatureMapper featureMapper;

    @Inject
    private FeatureService featureService;

    @Inject
    private FeatureSearchRepository featureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFeatureMockMvc;

    private Feature feature;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeatureResource featureResource = new FeatureResource();
        ReflectionTestUtils.setField(featureResource, "featureService", featureService);
        this.restFeatureMockMvc = MockMvcBuilders.standaloneSetup(featureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feature createEntity(EntityManager em) {
        Feature feature = new Feature();
        feature = new Feature()
                .code(DEFAULT_CODE)
                .label(DEFAULT_LABEL);
        return feature;
    }

    @Before
    public void initTest() {
        featureSearchRepository.deleteAll();
        feature = createEntity(em);
    }

    @Test
    @Transactional
    public void createFeature() throws Exception {
        int databaseSizeBeforeCreate = featureRepository.findAll().size();

        // Create the Feature
        FeatureDTO featureDTO = featureMapper.featureToFeatureDTO(feature);

        restFeatureMockMvc.perform(post("/api/features")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(featureDTO)))
                .andExpect(status().isCreated());

        // Validate the Feature in the database
        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeCreate + 1);
        Feature testFeature = features.get(features.size() - 1);
        assertThat(testFeature.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFeature.getLabel()).isEqualTo(DEFAULT_LABEL);

        // Validate the Feature in ElasticSearch
        Feature featureEs = featureSearchRepository.findOne(testFeature.getId());
        assertThat(featureEs).isEqualToComparingFieldByField(testFeature);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = featureRepository.findAll().size();
        // set the field null
        feature.setCode(null);

        // Create the Feature, which fails.
        FeatureDTO featureDTO = featureMapper.featureToFeatureDTO(feature);

        restFeatureMockMvc.perform(post("/api/features")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(featureDTO)))
                .andExpect(status().isBadRequest());

        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = featureRepository.findAll().size();
        // set the field null
        feature.setLabel(null);

        // Create the Feature, which fails.
        FeatureDTO featureDTO = featureMapper.featureToFeatureDTO(feature);

        restFeatureMockMvc.perform(post("/api/features")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(featureDTO)))
                .andExpect(status().isBadRequest());

        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFeatures() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

        // Get all the features
        restFeatureMockMvc.perform(get("/api/features?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(feature.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }

    @Test
    @Transactional
    public void getFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

        // Get the feature
        restFeatureMockMvc.perform(get("/api/features/{id}", feature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(feature.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFeature() throws Exception {
        // Get the feature
        restFeatureMockMvc.perform(get("/api/features/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);
        featureSearchRepository.save(feature);
        int databaseSizeBeforeUpdate = featureRepository.findAll().size();

        // Update the feature
        Feature updatedFeature = featureRepository.findOne(feature.getId());
        updatedFeature
                .code(UPDATED_CODE)
                .label(UPDATED_LABEL);
        FeatureDTO featureDTO = featureMapper.featureToFeatureDTO(updatedFeature);

        restFeatureMockMvc.perform(put("/api/features")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(featureDTO)))
                .andExpect(status().isOk());

        // Validate the Feature in the database
        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeUpdate);
        Feature testFeature = features.get(features.size() - 1);
        assertThat(testFeature.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFeature.getLabel()).isEqualTo(UPDATED_LABEL);

        // Validate the Feature in ElasticSearch
        Feature featureEs = featureSearchRepository.findOne(testFeature.getId());
        assertThat(featureEs).isEqualToComparingFieldByField(testFeature);
    }

    @Test
    @Transactional
    public void deleteFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);
        featureSearchRepository.save(feature);
        int databaseSizeBeforeDelete = featureRepository.findAll().size();

        // Get the feature
        restFeatureMockMvc.perform(delete("/api/features/{id}", feature.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean featureExistsInEs = featureSearchRepository.exists(feature.getId());
        assertThat(featureExistsInEs).isFalse();

        // Validate the database is empty
        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);
        featureSearchRepository.save(feature);

        // Search the feature
        restFeatureMockMvc.perform(get("/api/_search/features?query=id:" + feature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feature.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }
}
