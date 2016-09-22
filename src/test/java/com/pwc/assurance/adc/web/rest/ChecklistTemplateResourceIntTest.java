package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ChecklistTemplate;
import com.pwc.assurance.adc.repository.ChecklistTemplateRepository;
import com.pwc.assurance.adc.service.ChecklistTemplateService;
import com.pwc.assurance.adc.repository.search.ChecklistTemplateSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistTemplateDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistTemplateMapper;

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

import com.pwc.assurance.adc.domain.enumeration.ChecklistTemplateStatus;
/**
 * Test class for the ChecklistTemplateResource REST controller.
 *
 * @see ChecklistTemplateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ChecklistTemplateResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_VERSION = "AAAAA";
    private static final String UPDATED_VERSION = "BBBBB";

    private static final ChecklistTemplateStatus DEFAULT_STATUS = ChecklistTemplateStatus.DRAFT;
    private static final ChecklistTemplateStatus UPDATED_STATUS = ChecklistTemplateStatus.RELEASED;

    @Inject
    private ChecklistTemplateRepository checklistTemplateRepository;

    @Inject
    private ChecklistTemplateMapper checklistTemplateMapper;

    @Inject
    private ChecklistTemplateService checklistTemplateService;

    @Inject
    private ChecklistTemplateSearchRepository checklistTemplateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChecklistTemplateMockMvc;

    private ChecklistTemplate checklistTemplate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChecklistTemplateResource checklistTemplateResource = new ChecklistTemplateResource();
        ReflectionTestUtils.setField(checklistTemplateResource, "checklistTemplateService", checklistTemplateService);
        this.restChecklistTemplateMockMvc = MockMvcBuilders.standaloneSetup(checklistTemplateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChecklistTemplate createEntity(EntityManager em) {
        ChecklistTemplate checklistTemplate = new ChecklistTemplate();
        checklistTemplate = new ChecklistTemplate()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .version(DEFAULT_VERSION)
                .status(DEFAULT_STATUS);
        return checklistTemplate;
    }

    @Before
    public void initTest() {
        checklistTemplateSearchRepository.deleteAll();
        checklistTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createChecklistTemplate() throws Exception {
        int databaseSizeBeforeCreate = checklistTemplateRepository.findAll().size();

        // Create the ChecklistTemplate
        ChecklistTemplateDTO checklistTemplateDTO = checklistTemplateMapper.checklistTemplateToChecklistTemplateDTO(checklistTemplate);

        restChecklistTemplateMockMvc.perform(post("/api/checklist-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistTemplateDTO)))
                .andExpect(status().isCreated());

        // Validate the ChecklistTemplate in the database
        List<ChecklistTemplate> checklistTemplates = checklistTemplateRepository.findAll();
        assertThat(checklistTemplates).hasSize(databaseSizeBeforeCreate + 1);
        ChecklistTemplate testChecklistTemplate = checklistTemplates.get(checklistTemplates.size() - 1);
        assertThat(testChecklistTemplate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChecklistTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChecklistTemplate.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testChecklistTemplate.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the ChecklistTemplate in ElasticSearch
        ChecklistTemplate checklistTemplateEs = checklistTemplateSearchRepository.findOne(testChecklistTemplate.getId());
        assertThat(checklistTemplateEs).isEqualToComparingFieldByField(testChecklistTemplate);
    }

    @Test
    @Transactional
    public void getAllChecklistTemplates() throws Exception {
        // Initialize the database
        checklistTemplateRepository.saveAndFlush(checklistTemplate);

        // Get all the checklistTemplates
        restChecklistTemplateMockMvc.perform(get("/api/checklist-templates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(checklistTemplate.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getChecklistTemplate() throws Exception {
        // Initialize the database
        checklistTemplateRepository.saveAndFlush(checklistTemplate);

        // Get the checklistTemplate
        restChecklistTemplateMockMvc.perform(get("/api/checklist-templates/{id}", checklistTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checklistTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChecklistTemplate() throws Exception {
        // Get the checklistTemplate
        restChecklistTemplateMockMvc.perform(get("/api/checklist-templates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChecklistTemplate() throws Exception {
        // Initialize the database
        checklistTemplateRepository.saveAndFlush(checklistTemplate);
        checklistTemplateSearchRepository.save(checklistTemplate);
        int databaseSizeBeforeUpdate = checklistTemplateRepository.findAll().size();

        // Update the checklistTemplate
        ChecklistTemplate updatedChecklistTemplate = checklistTemplateRepository.findOne(checklistTemplate.getId());
        updatedChecklistTemplate
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .version(UPDATED_VERSION)
                .status(UPDATED_STATUS);
        ChecklistTemplateDTO checklistTemplateDTO = checklistTemplateMapper.checklistTemplateToChecklistTemplateDTO(updatedChecklistTemplate);

        restChecklistTemplateMockMvc.perform(put("/api/checklist-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistTemplateDTO)))
                .andExpect(status().isOk());

        // Validate the ChecklistTemplate in the database
        List<ChecklistTemplate> checklistTemplates = checklistTemplateRepository.findAll();
        assertThat(checklistTemplates).hasSize(databaseSizeBeforeUpdate);
        ChecklistTemplate testChecklistTemplate = checklistTemplates.get(checklistTemplates.size() - 1);
        assertThat(testChecklistTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChecklistTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChecklistTemplate.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testChecklistTemplate.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the ChecklistTemplate in ElasticSearch
        ChecklistTemplate checklistTemplateEs = checklistTemplateSearchRepository.findOne(testChecklistTemplate.getId());
        assertThat(checklistTemplateEs).isEqualToComparingFieldByField(testChecklistTemplate);
    }

    @Test
    @Transactional
    public void deleteChecklistTemplate() throws Exception {
        // Initialize the database
        checklistTemplateRepository.saveAndFlush(checklistTemplate);
        checklistTemplateSearchRepository.save(checklistTemplate);
        int databaseSizeBeforeDelete = checklistTemplateRepository.findAll().size();

        // Get the checklistTemplate
        restChecklistTemplateMockMvc.perform(delete("/api/checklist-templates/{id}", checklistTemplate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean checklistTemplateExistsInEs = checklistTemplateSearchRepository.exists(checklistTemplate.getId());
        assertThat(checklistTemplateExistsInEs).isFalse();

        // Validate the database is empty
        List<ChecklistTemplate> checklistTemplates = checklistTemplateRepository.findAll();
        assertThat(checklistTemplates).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChecklistTemplate() throws Exception {
        // Initialize the database
        checklistTemplateRepository.saveAndFlush(checklistTemplate);
        checklistTemplateSearchRepository.save(checklistTemplate);

        // Search the checklistTemplate
        restChecklistTemplateMockMvc.perform(get("/api/_search/checklist-templates?query=id:" + checklistTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checklistTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
