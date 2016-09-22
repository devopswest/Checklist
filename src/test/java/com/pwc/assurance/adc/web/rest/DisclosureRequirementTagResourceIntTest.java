package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.DisclosureRequirementTag;
import com.pwc.assurance.adc.repository.DisclosureRequirementTagRepository;
import com.pwc.assurance.adc.service.DisclosureRequirementTagService;
import com.pwc.assurance.adc.repository.search.DisclosureRequirementTagSearchRepository;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementTagDTO;
import com.pwc.assurance.adc.service.mapper.DisclosureRequirementTagMapper;

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
 * Test class for the DisclosureRequirementTagResource REST controller.
 *
 * @see DisclosureRequirementTagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class DisclosureRequirementTagResourceIntTest {
    private static final String DEFAULT_TAG_VALUE = "AAAAA";
    private static final String UPDATED_TAG_VALUE = "BBBBB";

    @Inject
    private DisclosureRequirementTagRepository disclosureRequirementTagRepository;

    @Inject
    private DisclosureRequirementTagMapper disclosureRequirementTagMapper;

    @Inject
    private DisclosureRequirementTagService disclosureRequirementTagService;

    @Inject
    private DisclosureRequirementTagSearchRepository disclosureRequirementTagSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDisclosureRequirementTagMockMvc;

    private DisclosureRequirementTag disclosureRequirementTag;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DisclosureRequirementTagResource disclosureRequirementTagResource = new DisclosureRequirementTagResource();
        ReflectionTestUtils.setField(disclosureRequirementTagResource, "disclosureRequirementTagService", disclosureRequirementTagService);
        this.restDisclosureRequirementTagMockMvc = MockMvcBuilders.standaloneSetup(disclosureRequirementTagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisclosureRequirementTag createEntity(EntityManager em) {
        DisclosureRequirementTag disclosureRequirementTag = new DisclosureRequirementTag();
        disclosureRequirementTag = new DisclosureRequirementTag()
                .tagValue(DEFAULT_TAG_VALUE);
        return disclosureRequirementTag;
    }

    @Before
    public void initTest() {
        disclosureRequirementTagSearchRepository.deleteAll();
        disclosureRequirementTag = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisclosureRequirementTag() throws Exception {
        int databaseSizeBeforeCreate = disclosureRequirementTagRepository.findAll().size();

        // Create the DisclosureRequirementTag
        DisclosureRequirementTagDTO disclosureRequirementTagDTO = disclosureRequirementTagMapper.disclosureRequirementTagToDisclosureRequirementTagDTO(disclosureRequirementTag);

        restDisclosureRequirementTagMockMvc.perform(post("/api/disclosure-requirement-tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disclosureRequirementTagDTO)))
                .andExpect(status().isCreated());

        // Validate the DisclosureRequirementTag in the database
        List<DisclosureRequirementTag> disclosureRequirementTags = disclosureRequirementTagRepository.findAll();
        assertThat(disclosureRequirementTags).hasSize(databaseSizeBeforeCreate + 1);
        DisclosureRequirementTag testDisclosureRequirementTag = disclosureRequirementTags.get(disclosureRequirementTags.size() - 1);
        assertThat(testDisclosureRequirementTag.getTagValue()).isEqualTo(DEFAULT_TAG_VALUE);

        // Validate the DisclosureRequirementTag in ElasticSearch
        DisclosureRequirementTag disclosureRequirementTagEs = disclosureRequirementTagSearchRepository.findOne(testDisclosureRequirementTag.getId());
        assertThat(disclosureRequirementTagEs).isEqualToComparingFieldByField(testDisclosureRequirementTag);
    }

    @Test
    @Transactional
    public void getAllDisclosureRequirementTags() throws Exception {
        // Initialize the database
        disclosureRequirementTagRepository.saveAndFlush(disclosureRequirementTag);

        // Get all the disclosureRequirementTags
        restDisclosureRequirementTagMockMvc.perform(get("/api/disclosure-requirement-tags?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(disclosureRequirementTag.getId().intValue())))
                .andExpect(jsonPath("$.[*].tagValue").value(hasItem(DEFAULT_TAG_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getDisclosureRequirementTag() throws Exception {
        // Initialize the database
        disclosureRequirementTagRepository.saveAndFlush(disclosureRequirementTag);

        // Get the disclosureRequirementTag
        restDisclosureRequirementTagMockMvc.perform(get("/api/disclosure-requirement-tags/{id}", disclosureRequirementTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disclosureRequirementTag.getId().intValue()))
            .andExpect(jsonPath("$.tagValue").value(DEFAULT_TAG_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDisclosureRequirementTag() throws Exception {
        // Get the disclosureRequirementTag
        restDisclosureRequirementTagMockMvc.perform(get("/api/disclosure-requirement-tags/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisclosureRequirementTag() throws Exception {
        // Initialize the database
        disclosureRequirementTagRepository.saveAndFlush(disclosureRequirementTag);
        disclosureRequirementTagSearchRepository.save(disclosureRequirementTag);
        int databaseSizeBeforeUpdate = disclosureRequirementTagRepository.findAll().size();

        // Update the disclosureRequirementTag
        DisclosureRequirementTag updatedDisclosureRequirementTag = disclosureRequirementTagRepository.findOne(disclosureRequirementTag.getId());
        updatedDisclosureRequirementTag
                .tagValue(UPDATED_TAG_VALUE);
        DisclosureRequirementTagDTO disclosureRequirementTagDTO = disclosureRequirementTagMapper.disclosureRequirementTagToDisclosureRequirementTagDTO(updatedDisclosureRequirementTag);

        restDisclosureRequirementTagMockMvc.perform(put("/api/disclosure-requirement-tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disclosureRequirementTagDTO)))
                .andExpect(status().isOk());

        // Validate the DisclosureRequirementTag in the database
        List<DisclosureRequirementTag> disclosureRequirementTags = disclosureRequirementTagRepository.findAll();
        assertThat(disclosureRequirementTags).hasSize(databaseSizeBeforeUpdate);
        DisclosureRequirementTag testDisclosureRequirementTag = disclosureRequirementTags.get(disclosureRequirementTags.size() - 1);
        assertThat(testDisclosureRequirementTag.getTagValue()).isEqualTo(UPDATED_TAG_VALUE);

        // Validate the DisclosureRequirementTag in ElasticSearch
        DisclosureRequirementTag disclosureRequirementTagEs = disclosureRequirementTagSearchRepository.findOne(testDisclosureRequirementTag.getId());
        assertThat(disclosureRequirementTagEs).isEqualToComparingFieldByField(testDisclosureRequirementTag);
    }

    @Test
    @Transactional
    public void deleteDisclosureRequirementTag() throws Exception {
        // Initialize the database
        disclosureRequirementTagRepository.saveAndFlush(disclosureRequirementTag);
        disclosureRequirementTagSearchRepository.save(disclosureRequirementTag);
        int databaseSizeBeforeDelete = disclosureRequirementTagRepository.findAll().size();

        // Get the disclosureRequirementTag
        restDisclosureRequirementTagMockMvc.perform(delete("/api/disclosure-requirement-tags/{id}", disclosureRequirementTag.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean disclosureRequirementTagExistsInEs = disclosureRequirementTagSearchRepository.exists(disclosureRequirementTag.getId());
        assertThat(disclosureRequirementTagExistsInEs).isFalse();

        // Validate the database is empty
        List<DisclosureRequirementTag> disclosureRequirementTags = disclosureRequirementTagRepository.findAll();
        assertThat(disclosureRequirementTags).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDisclosureRequirementTag() throws Exception {
        // Initialize the database
        disclosureRequirementTagRepository.saveAndFlush(disclosureRequirementTag);
        disclosureRequirementTagSearchRepository.save(disclosureRequirementTag);

        // Search the disclosureRequirementTag
        restDisclosureRequirementTagMockMvc.perform(get("/api/_search/disclosure-requirement-tags?query=id:" + disclosureRequirementTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disclosureRequirementTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagValue").value(hasItem(DEFAULT_TAG_VALUE.toString())));
    }
}
