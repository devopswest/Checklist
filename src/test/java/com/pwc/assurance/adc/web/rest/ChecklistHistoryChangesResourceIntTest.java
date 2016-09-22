package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ChecklistHistoryChanges;
import com.pwc.assurance.adc.repository.ChecklistHistoryChangesRepository;
import com.pwc.assurance.adc.service.ChecklistHistoryChangesService;
import com.pwc.assurance.adc.repository.search.ChecklistHistoryChangesSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistHistoryChangesDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistHistoryChangesMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChecklistHistoryChangesResource REST controller.
 *
 * @see ChecklistHistoryChangesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ChecklistHistoryChangesResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_HAPPENED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HAPPENED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HAPPENED_STR = dateTimeFormatter.format(DEFAULT_HAPPENED);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ChecklistHistoryChangesRepository checklistHistoryChangesRepository;

    @Inject
    private ChecklistHistoryChangesMapper checklistHistoryChangesMapper;

    @Inject
    private ChecklistHistoryChangesService checklistHistoryChangesService;

    @Inject
    private ChecklistHistoryChangesSearchRepository checklistHistoryChangesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChecklistHistoryChangesMockMvc;

    private ChecklistHistoryChanges checklistHistoryChanges;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChecklistHistoryChangesResource checklistHistoryChangesResource = new ChecklistHistoryChangesResource();
        ReflectionTestUtils.setField(checklistHistoryChangesResource, "checklistHistoryChangesService", checklistHistoryChangesService);
        this.restChecklistHistoryChangesMockMvc = MockMvcBuilders.standaloneSetup(checklistHistoryChangesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChecklistHistoryChanges createEntity(EntityManager em) {
        ChecklistHistoryChanges checklistHistoryChanges = new ChecklistHistoryChanges();
        checklistHistoryChanges = new ChecklistHistoryChanges()
                .happened(DEFAULT_HAPPENED)
                .description(DEFAULT_DESCRIPTION);
        return checklistHistoryChanges;
    }

    @Before
    public void initTest() {
        checklistHistoryChangesSearchRepository.deleteAll();
        checklistHistoryChanges = createEntity(em);
    }

    @Test
    @Transactional
    public void createChecklistHistoryChanges() throws Exception {
        int databaseSizeBeforeCreate = checklistHistoryChangesRepository.findAll().size();

        // Create the ChecklistHistoryChanges
        ChecklistHistoryChangesDTO checklistHistoryChangesDTO = checklistHistoryChangesMapper.checklistHistoryChangesToChecklistHistoryChangesDTO(checklistHistoryChanges);

        restChecklistHistoryChangesMockMvc.perform(post("/api/checklist-history-changes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistHistoryChangesDTO)))
                .andExpect(status().isCreated());

        // Validate the ChecklistHistoryChanges in the database
        List<ChecklistHistoryChanges> checklistHistoryChanges = checklistHistoryChangesRepository.findAll();
        assertThat(checklistHistoryChanges).hasSize(databaseSizeBeforeCreate + 1);
        ChecklistHistoryChanges testChecklistHistoryChanges = checklistHistoryChanges.get(checklistHistoryChanges.size() - 1);
        assertThat(testChecklistHistoryChanges.getHappened()).isEqualTo(DEFAULT_HAPPENED);
        assertThat(testChecklistHistoryChanges.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ChecklistHistoryChanges in ElasticSearch
        ChecklistHistoryChanges checklistHistoryChangesEs = checklistHistoryChangesSearchRepository.findOne(testChecklistHistoryChanges.getId());
        assertThat(checklistHistoryChangesEs).isEqualToComparingFieldByField(testChecklistHistoryChanges);
    }

    @Test
    @Transactional
    public void getAllChecklistHistoryChanges() throws Exception {
        // Initialize the database
        checklistHistoryChangesRepository.saveAndFlush(checklistHistoryChanges);

        // Get all the checklistHistoryChanges
        restChecklistHistoryChangesMockMvc.perform(get("/api/checklist-history-changes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(checklistHistoryChanges.getId().intValue())))
                .andExpect(jsonPath("$.[*].happened").value(hasItem(DEFAULT_HAPPENED_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getChecklistHistoryChanges() throws Exception {
        // Initialize the database
        checklistHistoryChangesRepository.saveAndFlush(checklistHistoryChanges);

        // Get the checklistHistoryChanges
        restChecklistHistoryChangesMockMvc.perform(get("/api/checklist-history-changes/{id}", checklistHistoryChanges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checklistHistoryChanges.getId().intValue()))
            .andExpect(jsonPath("$.happened").value(DEFAULT_HAPPENED_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChecklistHistoryChanges() throws Exception {
        // Get the checklistHistoryChanges
        restChecklistHistoryChangesMockMvc.perform(get("/api/checklist-history-changes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChecklistHistoryChanges() throws Exception {
        // Initialize the database
        checklistHistoryChangesRepository.saveAndFlush(checklistHistoryChanges);
        checklistHistoryChangesSearchRepository.save(checklistHistoryChanges);
        int databaseSizeBeforeUpdate = checklistHistoryChangesRepository.findAll().size();

        // Update the checklistHistoryChanges
        ChecklistHistoryChanges updatedChecklistHistoryChanges = checklistHistoryChangesRepository.findOne(checklistHistoryChanges.getId());
        updatedChecklistHistoryChanges
                .happened(UPDATED_HAPPENED)
                .description(UPDATED_DESCRIPTION);
        ChecklistHistoryChangesDTO checklistHistoryChangesDTO = checklistHistoryChangesMapper.checklistHistoryChangesToChecklistHistoryChangesDTO(updatedChecklistHistoryChanges);

        restChecklistHistoryChangesMockMvc.perform(put("/api/checklist-history-changes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistHistoryChangesDTO)))
                .andExpect(status().isOk());

        // Validate the ChecklistHistoryChanges in the database
        List<ChecklistHistoryChanges> checklistHistoryChanges = checklistHistoryChangesRepository.findAll();
        assertThat(checklistHistoryChanges).hasSize(databaseSizeBeforeUpdate);
        ChecklistHistoryChanges testChecklistHistoryChanges = checklistHistoryChanges.get(checklistHistoryChanges.size() - 1);
        assertThat(testChecklistHistoryChanges.getHappened()).isEqualTo(UPDATED_HAPPENED);
        assertThat(testChecklistHistoryChanges.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ChecklistHistoryChanges in ElasticSearch
        ChecklistHistoryChanges checklistHistoryChangesEs = checklistHistoryChangesSearchRepository.findOne(testChecklistHistoryChanges.getId());
        assertThat(checklistHistoryChangesEs).isEqualToComparingFieldByField(testChecklistHistoryChanges);
    }

    @Test
    @Transactional
    public void deleteChecklistHistoryChanges() throws Exception {
        // Initialize the database
        checklistHistoryChangesRepository.saveAndFlush(checklistHistoryChanges);
        checklistHistoryChangesSearchRepository.save(checklistHistoryChanges);
        int databaseSizeBeforeDelete = checklistHistoryChangesRepository.findAll().size();

        // Get the checklistHistoryChanges
        restChecklistHistoryChangesMockMvc.perform(delete("/api/checklist-history-changes/{id}", checklistHistoryChanges.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean checklistHistoryChangesExistsInEs = checklistHistoryChangesSearchRepository.exists(checklistHistoryChanges.getId());
        assertThat(checklistHistoryChangesExistsInEs).isFalse();

        // Validate the database is empty
        List<ChecklistHistoryChanges> checklistHistoryChanges = checklistHistoryChangesRepository.findAll();
        assertThat(checklistHistoryChanges).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChecklistHistoryChanges() throws Exception {
        // Initialize the database
        checklistHistoryChangesRepository.saveAndFlush(checklistHistoryChanges);
        checklistHistoryChangesSearchRepository.save(checklistHistoryChanges);

        // Search the checklistHistoryChanges
        restChecklistHistoryChangesMockMvc.perform(get("/api/_search/checklist-history-changes?query=id:" + checklistHistoryChanges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checklistHistoryChanges.getId().intValue())))
            .andExpect(jsonPath("$.[*].happened").value(hasItem(DEFAULT_HAPPENED_STR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
