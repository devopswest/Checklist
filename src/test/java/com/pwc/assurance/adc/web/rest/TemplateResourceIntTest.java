package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Template;
import com.pwc.assurance.adc.repository.TemplateRepository;
import com.pwc.assurance.adc.service.TemplateService;
import com.pwc.assurance.adc.repository.search.TemplateSearchRepository;
import com.pwc.assurance.adc.service.dto.TemplateDTO;
import com.pwc.assurance.adc.service.mapper.TemplateMapper;

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
 * Test class for the TemplateResource REST controller.
 *
 * @see TemplateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class TemplateResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_CONTENT = "A";
    private static final String UPDATED_CONTENT = "B";

    @Inject
    private TemplateRepository templateRepository;

    @Inject
    private TemplateMapper templateMapper;

    @Inject
    private TemplateService templateService;

    @Inject
    private TemplateSearchRepository templateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTemplateMockMvc;

    private Template template;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TemplateResource templateResource = new TemplateResource();
        ReflectionTestUtils.setField(templateResource, "templateService", templateService);
        this.restTemplateMockMvc = MockMvcBuilders.standaloneSetup(templateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Template createEntity(EntityManager em) {
        Template template = new Template();
        template = new Template()
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION)
                .content(DEFAULT_CONTENT);
        return template;
    }

    @Before
    public void initTest() {
        templateSearchRepository.deleteAll();
        template = createEntity(em);
    }

    @Test
    @Transactional
    public void createTemplate() throws Exception {
        int databaseSizeBeforeCreate = templateRepository.findAll().size();

        // Create the Template
        TemplateDTO templateDTO = templateMapper.templateToTemplateDTO(template);

        restTemplateMockMvc.perform(post("/api/templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templateDTO)))
                .andExpect(status().isCreated());

        // Validate the Template in the database
        List<Template> templates = templateRepository.findAll();
        assertThat(templates).hasSize(databaseSizeBeforeCreate + 1);
        Template testTemplate = templates.get(templates.size() - 1);
        assertThat(testTemplate.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTemplate.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the Template in ElasticSearch
        Template templateEs = templateSearchRepository.findOne(testTemplate.getId());
        assertThat(templateEs).isEqualToComparingFieldByField(testTemplate);
    }

    @Test
    @Transactional
    public void getAllTemplates() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templates
        restTemplateMockMvc.perform(get("/api/templates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(template.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get the template
        restTemplateMockMvc.perform(get("/api/templates/{id}", template.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(template.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTemplate() throws Exception {
        // Get the template
        restTemplateMockMvc.perform(get("/api/templates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);
        templateSearchRepository.save(template);
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();

        // Update the template
        Template updatedTemplate = templateRepository.findOne(template.getId());
        updatedTemplate
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION)
                .content(UPDATED_CONTENT);
        TemplateDTO templateDTO = templateMapper.templateToTemplateDTO(updatedTemplate);

        restTemplateMockMvc.perform(put("/api/templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(templateDTO)))
                .andExpect(status().isOk());

        // Validate the Template in the database
        List<Template> templates = templateRepository.findAll();
        assertThat(templates).hasSize(databaseSizeBeforeUpdate);
        Template testTemplate = templates.get(templates.size() - 1);
        assertThat(testTemplate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTemplate.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the Template in ElasticSearch
        Template templateEs = templateSearchRepository.findOne(testTemplate.getId());
        assertThat(templateEs).isEqualToComparingFieldByField(testTemplate);
    }

    @Test
    @Transactional
    public void deleteTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);
        templateSearchRepository.save(template);
        int databaseSizeBeforeDelete = templateRepository.findAll().size();

        // Get the template
        restTemplateMockMvc.perform(delete("/api/templates/{id}", template.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean templateExistsInEs = templateSearchRepository.exists(template.getId());
        assertThat(templateExistsInEs).isFalse();

        // Validate the database is empty
        List<Template> templates = templateRepository.findAll();
        assertThat(templates).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);
        templateSearchRepository.save(template);

        // Search the template
        restTemplateMockMvc.perform(get("/api/_search/templates?query=id:" + template.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(template.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }
}
