package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.Checklist;
import com.pwc.assurance.adc.repository.ChecklistRepository;
import com.pwc.assurance.adc.service.ChecklistService;
import com.pwc.assurance.adc.repository.search.ChecklistSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistMapper;

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

import com.pwc.assurance.adc.domain.enumeration.ResponseStatus;
/**
 * Test class for the ChecklistResource REST controller.
 *
 * @see ChecklistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ChecklistResourceIntTest {
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ResponseStatus DEFAULT_STATUS = ResponseStatus.DRAFT;
    private static final ResponseStatus UPDATED_STATUS = ResponseStatus.FINAL;

    @Inject
    private ChecklistRepository checklistRepository;

    @Inject
    private ChecklistMapper checklistMapper;

    @Inject
    private ChecklistService checklistService;

    @Inject
    private ChecklistSearchRepository checklistSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChecklistMockMvc;

    private Checklist checklist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChecklistResource checklistResource = new ChecklistResource();
        ReflectionTestUtils.setField(checklistResource, "checklistService", checklistService);
        this.restChecklistMockMvc = MockMvcBuilders.standaloneSetup(checklistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checklist createEntity(EntityManager em) {
        Checklist checklist = new Checklist();
        checklist = new Checklist()
                .description(DEFAULT_DESCRIPTION)
                .status(DEFAULT_STATUS);
        return checklist;
    }

    @Before
    public void initTest() {
        checklistSearchRepository.deleteAll();
        checklist = createEntity(em);
    }

    @Test
    @Transactional
    public void createChecklist() throws Exception {
        int databaseSizeBeforeCreate = checklistRepository.findAll().size();

        // Create the Checklist
        ChecklistDTO checklistDTO = checklistMapper.checklistToChecklistDTO(checklist);

        restChecklistMockMvc.perform(post("/api/checklists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistDTO)))
                .andExpect(status().isCreated());

        // Validate the Checklist in the database
        List<Checklist> checklists = checklistRepository.findAll();
        assertThat(checklists).hasSize(databaseSizeBeforeCreate + 1);
        Checklist testChecklist = checklists.get(checklists.size() - 1);
        assertThat(testChecklist.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChecklist.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Checklist in ElasticSearch
        Checklist checklistEs = checklistSearchRepository.findOne(testChecklist.getId());
        assertThat(checklistEs).isEqualToComparingFieldByField(testChecklist);
    }

    @Test
    @Transactional
    public void getAllChecklists() throws Exception {
        // Initialize the database
        checklistRepository.saveAndFlush(checklist);

        // Get all the checklists
        restChecklistMockMvc.perform(get("/api/checklists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(checklist.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getChecklist() throws Exception {
        // Initialize the database
        checklistRepository.saveAndFlush(checklist);

        // Get the checklist
        restChecklistMockMvc.perform(get("/api/checklists/{id}", checklist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checklist.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChecklist() throws Exception {
        // Get the checklist
        restChecklistMockMvc.perform(get("/api/checklists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChecklist() throws Exception {
        // Initialize the database
        checklistRepository.saveAndFlush(checklist);
        checklistSearchRepository.save(checklist);
        int databaseSizeBeforeUpdate = checklistRepository.findAll().size();

        // Update the checklist
        Checklist updatedChecklist = checklistRepository.findOne(checklist.getId());
        updatedChecklist
                .description(UPDATED_DESCRIPTION)
                .status(UPDATED_STATUS);
        ChecklistDTO checklistDTO = checklistMapper.checklistToChecklistDTO(updatedChecklist);

        restChecklistMockMvc.perform(put("/api/checklists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistDTO)))
                .andExpect(status().isOk());

        // Validate the Checklist in the database
        List<Checklist> checklists = checklistRepository.findAll();
        assertThat(checklists).hasSize(databaseSizeBeforeUpdate);
        Checklist testChecklist = checklists.get(checklists.size() - 1);
        assertThat(testChecklist.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChecklist.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Checklist in ElasticSearch
        Checklist checklistEs = checklistSearchRepository.findOne(testChecklist.getId());
        assertThat(checklistEs).isEqualToComparingFieldByField(testChecklist);
    }

    @Test
    @Transactional
    public void deleteChecklist() throws Exception {
        // Initialize the database
        checklistRepository.saveAndFlush(checklist);
        checklistSearchRepository.save(checklist);
        int databaseSizeBeforeDelete = checklistRepository.findAll().size();

        // Get the checklist
        restChecklistMockMvc.perform(delete("/api/checklists/{id}", checklist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean checklistExistsInEs = checklistSearchRepository.exists(checklist.getId());
        assertThat(checklistExistsInEs).isFalse();

        // Validate the database is empty
        List<Checklist> checklists = checklistRepository.findAll();
        assertThat(checklists).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChecklist() throws Exception {
        // Initialize the database
        checklistRepository.saveAndFlush(checklist);
        checklistSearchRepository.save(checklist);

        // Search the checklist
        restChecklistMockMvc.perform(get("/api/_search/checklists?query=id:" + checklist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checklist.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
