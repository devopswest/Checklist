package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.EngagementChecklistTemplate;
import com.pwc.assurance.adc.repository.EngagementChecklistTemplateRepository;
import com.pwc.assurance.adc.service.EngagementChecklistTemplateService;
import com.pwc.assurance.adc.repository.search.EngagementChecklistTemplateSearchRepository;
import com.pwc.assurance.adc.service.dto.EngagementChecklistTemplateDTO;
import com.pwc.assurance.adc.service.mapper.EngagementChecklistTemplateMapper;

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
 * Test class for the EngagementChecklistTemplateResource REST controller.
 *
 * @see EngagementChecklistTemplateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class EngagementChecklistTemplateResourceIntTest {

    @Inject
    private EngagementChecklistTemplateRepository engagementChecklistTemplateRepository;

    @Inject
    private EngagementChecklistTemplateMapper engagementChecklistTemplateMapper;

    @Inject
    private EngagementChecklistTemplateService engagementChecklistTemplateService;

    @Inject
    private EngagementChecklistTemplateSearchRepository engagementChecklistTemplateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEngagementChecklistTemplateMockMvc;

    private EngagementChecklistTemplate engagementChecklistTemplate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EngagementChecklistTemplateResource engagementChecklistTemplateResource = new EngagementChecklistTemplateResource();
        ReflectionTestUtils.setField(engagementChecklistTemplateResource, "engagementChecklistTemplateService", engagementChecklistTemplateService);
        this.restEngagementChecklistTemplateMockMvc = MockMvcBuilders.standaloneSetup(engagementChecklistTemplateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EngagementChecklistTemplate createEntity(EntityManager em) {
        EngagementChecklistTemplate engagementChecklistTemplate = new EngagementChecklistTemplate();
        engagementChecklistTemplate = new EngagementChecklistTemplate();
        return engagementChecklistTemplate;
    }

    @Before
    public void initTest() {
        engagementChecklistTemplateSearchRepository.deleteAll();
        engagementChecklistTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createEngagementChecklistTemplate() throws Exception {
        int databaseSizeBeforeCreate = engagementChecklistTemplateRepository.findAll().size();

        // Create the EngagementChecklistTemplate
        EngagementChecklistTemplateDTO engagementChecklistTemplateDTO = engagementChecklistTemplateMapper.engagementChecklistTemplateToEngagementChecklistTemplateDTO(engagementChecklistTemplate);

        restEngagementChecklistTemplateMockMvc.perform(post("/api/engagement-checklist-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engagementChecklistTemplateDTO)))
                .andExpect(status().isCreated());

        // Validate the EngagementChecklistTemplate in the database
        List<EngagementChecklistTemplate> engagementChecklistTemplates = engagementChecklistTemplateRepository.findAll();
        assertThat(engagementChecklistTemplates).hasSize(databaseSizeBeforeCreate + 1);
        EngagementChecklistTemplate testEngagementChecklistTemplate = engagementChecklistTemplates.get(engagementChecklistTemplates.size() - 1);

        // Validate the EngagementChecklistTemplate in ElasticSearch
        EngagementChecklistTemplate engagementChecklistTemplateEs = engagementChecklistTemplateSearchRepository.findOne(testEngagementChecklistTemplate.getId());
        assertThat(engagementChecklistTemplateEs).isEqualToComparingFieldByField(testEngagementChecklistTemplate);
    }

    @Test
    @Transactional
    public void getAllEngagementChecklistTemplates() throws Exception {
        // Initialize the database
        engagementChecklistTemplateRepository.saveAndFlush(engagementChecklistTemplate);

        // Get all the engagementChecklistTemplates
        restEngagementChecklistTemplateMockMvc.perform(get("/api/engagement-checklist-templates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(engagementChecklistTemplate.getId().intValue())));
    }

    @Test
    @Transactional
    public void getEngagementChecklistTemplate() throws Exception {
        // Initialize the database
        engagementChecklistTemplateRepository.saveAndFlush(engagementChecklistTemplate);

        // Get the engagementChecklistTemplate
        restEngagementChecklistTemplateMockMvc.perform(get("/api/engagement-checklist-templates/{id}", engagementChecklistTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(engagementChecklistTemplate.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEngagementChecklistTemplate() throws Exception {
        // Get the engagementChecklistTemplate
        restEngagementChecklistTemplateMockMvc.perform(get("/api/engagement-checklist-templates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEngagementChecklistTemplate() throws Exception {
        // Initialize the database
        engagementChecklistTemplateRepository.saveAndFlush(engagementChecklistTemplate);
        engagementChecklistTemplateSearchRepository.save(engagementChecklistTemplate);
        int databaseSizeBeforeUpdate = engagementChecklistTemplateRepository.findAll().size();

        // Update the engagementChecklistTemplate
        EngagementChecklistTemplate updatedEngagementChecklistTemplate = engagementChecklistTemplateRepository.findOne(engagementChecklistTemplate.getId());
        EngagementChecklistTemplateDTO engagementChecklistTemplateDTO = engagementChecklistTemplateMapper.engagementChecklistTemplateToEngagementChecklistTemplateDTO(updatedEngagementChecklistTemplate);

        restEngagementChecklistTemplateMockMvc.perform(put("/api/engagement-checklist-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engagementChecklistTemplateDTO)))
                .andExpect(status().isOk());

        // Validate the EngagementChecklistTemplate in the database
        List<EngagementChecklistTemplate> engagementChecklistTemplates = engagementChecklistTemplateRepository.findAll();
        assertThat(engagementChecklistTemplates).hasSize(databaseSizeBeforeUpdate);
        EngagementChecklistTemplate testEngagementChecklistTemplate = engagementChecklistTemplates.get(engagementChecklistTemplates.size() - 1);

        // Validate the EngagementChecklistTemplate in ElasticSearch
        EngagementChecklistTemplate engagementChecklistTemplateEs = engagementChecklistTemplateSearchRepository.findOne(testEngagementChecklistTemplate.getId());
        assertThat(engagementChecklistTemplateEs).isEqualToComparingFieldByField(testEngagementChecklistTemplate);
    }

    @Test
    @Transactional
    public void deleteEngagementChecklistTemplate() throws Exception {
        // Initialize the database
        engagementChecklistTemplateRepository.saveAndFlush(engagementChecklistTemplate);
        engagementChecklistTemplateSearchRepository.save(engagementChecklistTemplate);
        int databaseSizeBeforeDelete = engagementChecklistTemplateRepository.findAll().size();

        // Get the engagementChecklistTemplate
        restEngagementChecklistTemplateMockMvc.perform(delete("/api/engagement-checklist-templates/{id}", engagementChecklistTemplate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean engagementChecklistTemplateExistsInEs = engagementChecklistTemplateSearchRepository.exists(engagementChecklistTemplate.getId());
        assertThat(engagementChecklistTemplateExistsInEs).isFalse();

        // Validate the database is empty
        List<EngagementChecklistTemplate> engagementChecklistTemplates = engagementChecklistTemplateRepository.findAll();
        assertThat(engagementChecklistTemplates).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEngagementChecklistTemplate() throws Exception {
        // Initialize the database
        engagementChecklistTemplateRepository.saveAndFlush(engagementChecklistTemplate);
        engagementChecklistTemplateSearchRepository.save(engagementChecklistTemplate);

        // Search the engagementChecklistTemplate
        restEngagementChecklistTemplateMockMvc.perform(get("/api/_search/engagement-checklist-templates?query=id:" + engagementChecklistTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engagementChecklistTemplate.getId().intValue())));
    }
}
