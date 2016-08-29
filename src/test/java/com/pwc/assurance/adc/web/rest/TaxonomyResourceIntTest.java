package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Taxonomy;
import com.pwc.assurance.adc.repository.TaxonomyRepository;
import com.pwc.assurance.adc.repository.search.TaxonomySearchRepository;

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
 * Test class for the TaxonomyResource REST controller.
 *
 * @see TaxonomyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class TaxonomyResourceIntTest {
    private static final String DEFAULT_CODE = "A";
    private static final String UPDATED_CODE = "B";
    private static final String DEFAULT_LABEL = "A";
    private static final String UPDATED_LABEL = "B";

    @Inject
    private TaxonomyRepository taxonomyRepository;

    @Inject
    private TaxonomySearchRepository taxonomySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTaxonomyMockMvc;

    private Taxonomy taxonomy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaxonomyResource taxonomyResource = new TaxonomyResource();
        ReflectionTestUtils.setField(taxonomyResource, "taxonomySearchRepository", taxonomySearchRepository);
        ReflectionTestUtils.setField(taxonomyResource, "taxonomyRepository", taxonomyRepository);
        this.restTaxonomyMockMvc = MockMvcBuilders.standaloneSetup(taxonomyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taxonomy createEntity(EntityManager em) {
        Taxonomy taxonomy = new Taxonomy();
        taxonomy = new Taxonomy()
                .code(DEFAULT_CODE)
                .label(DEFAULT_LABEL);
        return taxonomy;
    }

    @Before
    public void initTest() {
        taxonomySearchRepository.deleteAll();
        taxonomy = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaxonomy() throws Exception {
        int databaseSizeBeforeCreate = taxonomyRepository.findAll().size();

        // Create the Taxonomy

        restTaxonomyMockMvc.perform(post("/api/taxonomies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taxonomy)))
                .andExpect(status().isCreated());

        // Validate the Taxonomy in the database
        List<Taxonomy> taxonomies = taxonomyRepository.findAll();
        assertThat(taxonomies).hasSize(databaseSizeBeforeCreate + 1);
        Taxonomy testTaxonomy = taxonomies.get(taxonomies.size() - 1);
        assertThat(testTaxonomy.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTaxonomy.getLabel()).isEqualTo(DEFAULT_LABEL);

        // Validate the Taxonomy in ElasticSearch
        Taxonomy taxonomyEs = taxonomySearchRepository.findOne(testTaxonomy.getId());
        assertThat(taxonomyEs).isEqualToComparingFieldByField(testTaxonomy);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxonomyRepository.findAll().size();
        // set the field null
        taxonomy.setCode(null);

        // Create the Taxonomy, which fails.

        restTaxonomyMockMvc.perform(post("/api/taxonomies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taxonomy)))
                .andExpect(status().isBadRequest());

        List<Taxonomy> taxonomies = taxonomyRepository.findAll();
        assertThat(taxonomies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxonomyRepository.findAll().size();
        // set the field null
        taxonomy.setLabel(null);

        // Create the Taxonomy, which fails.

        restTaxonomyMockMvc.perform(post("/api/taxonomies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taxonomy)))
                .andExpect(status().isBadRequest());

        List<Taxonomy> taxonomies = taxonomyRepository.findAll();
        assertThat(taxonomies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaxonomies() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);

        // Get all the taxonomies
        restTaxonomyMockMvc.perform(get("/api/taxonomies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taxonomy.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }

    @Test
    @Transactional
    public void getTaxonomy() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);

        // Get the taxonomy
        restTaxonomyMockMvc.perform(get("/api/taxonomies/{id}", taxonomy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taxonomy.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaxonomy() throws Exception {
        // Get the taxonomy
        restTaxonomyMockMvc.perform(get("/api/taxonomies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaxonomy() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);
        taxonomySearchRepository.save(taxonomy);
        int databaseSizeBeforeUpdate = taxonomyRepository.findAll().size();

        // Update the taxonomy
        Taxonomy updatedTaxonomy = taxonomyRepository.findOne(taxonomy.getId());
        updatedTaxonomy
                .code(UPDATED_CODE)
                .label(UPDATED_LABEL);

        restTaxonomyMockMvc.perform(put("/api/taxonomies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTaxonomy)))
                .andExpect(status().isOk());

        // Validate the Taxonomy in the database
        List<Taxonomy> taxonomies = taxonomyRepository.findAll();
        assertThat(taxonomies).hasSize(databaseSizeBeforeUpdate);
        Taxonomy testTaxonomy = taxonomies.get(taxonomies.size() - 1);
        assertThat(testTaxonomy.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTaxonomy.getLabel()).isEqualTo(UPDATED_LABEL);

        // Validate the Taxonomy in ElasticSearch
        Taxonomy taxonomyEs = taxonomySearchRepository.findOne(testTaxonomy.getId());
        assertThat(taxonomyEs).isEqualToComparingFieldByField(testTaxonomy);
    }

    @Test
    @Transactional
    public void deleteTaxonomy() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);
        taxonomySearchRepository.save(taxonomy);
        int databaseSizeBeforeDelete = taxonomyRepository.findAll().size();

        // Get the taxonomy
        restTaxonomyMockMvc.perform(delete("/api/taxonomies/{id}", taxonomy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean taxonomyExistsInEs = taxonomySearchRepository.exists(taxonomy.getId());
        assertThat(taxonomyExistsInEs).isFalse();

        // Validate the database is empty
        List<Taxonomy> taxonomies = taxonomyRepository.findAll();
        assertThat(taxonomies).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTaxonomy() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);
        taxonomySearchRepository.save(taxonomy);

        // Search the taxonomy
        restTaxonomyMockMvc.perform(get("/api/_search/taxonomies?query=id:" + taxonomy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxonomy.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }
}
