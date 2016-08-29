package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.AuditProfileLogEntry;
import com.pwc.assurance.adc.repository.AuditProfileLogEntryRepository;
import com.pwc.assurance.adc.repository.search.AuditProfileLogEntrySearchRepository;

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
 * Test class for the AuditProfileLogEntryResource REST controller.
 *
 * @see AuditProfileLogEntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class AuditProfileLogEntryResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_HAPPENED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HAPPENED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HAPPENED_STR = dateTimeFormatter.format(DEFAULT_HAPPENED);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private AuditProfileLogEntryRepository auditProfileLogEntryRepository;

    @Inject
    private AuditProfileLogEntrySearchRepository auditProfileLogEntrySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAuditProfileLogEntryMockMvc;

    private AuditProfileLogEntry auditProfileLogEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuditProfileLogEntryResource auditProfileLogEntryResource = new AuditProfileLogEntryResource();
        ReflectionTestUtils.setField(auditProfileLogEntryResource, "auditProfileLogEntrySearchRepository", auditProfileLogEntrySearchRepository);
        ReflectionTestUtils.setField(auditProfileLogEntryResource, "auditProfileLogEntryRepository", auditProfileLogEntryRepository);
        this.restAuditProfileLogEntryMockMvc = MockMvcBuilders.standaloneSetup(auditProfileLogEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditProfileLogEntry createEntity(EntityManager em) {
        AuditProfileLogEntry auditProfileLogEntry = new AuditProfileLogEntry();
        auditProfileLogEntry = new AuditProfileLogEntry()
                .happened(DEFAULT_HAPPENED)
                .description(DEFAULT_DESCRIPTION);
        return auditProfileLogEntry;
    }

    @Before
    public void initTest() {
        auditProfileLogEntrySearchRepository.deleteAll();
        auditProfileLogEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditProfileLogEntry() throws Exception {
        int databaseSizeBeforeCreate = auditProfileLogEntryRepository.findAll().size();

        // Create the AuditProfileLogEntry

        restAuditProfileLogEntryMockMvc.perform(post("/api/audit-profile-log-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auditProfileLogEntry)))
                .andExpect(status().isCreated());

        // Validate the AuditProfileLogEntry in the database
        List<AuditProfileLogEntry> auditProfileLogEntries = auditProfileLogEntryRepository.findAll();
        assertThat(auditProfileLogEntries).hasSize(databaseSizeBeforeCreate + 1);
        AuditProfileLogEntry testAuditProfileLogEntry = auditProfileLogEntries.get(auditProfileLogEntries.size() - 1);
        assertThat(testAuditProfileLogEntry.getHappened()).isEqualTo(DEFAULT_HAPPENED);
        assertThat(testAuditProfileLogEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the AuditProfileLogEntry in ElasticSearch
        AuditProfileLogEntry auditProfileLogEntryEs = auditProfileLogEntrySearchRepository.findOne(testAuditProfileLogEntry.getId());
        assertThat(auditProfileLogEntryEs).isEqualToComparingFieldByField(testAuditProfileLogEntry);
    }

    @Test
    @Transactional
    public void getAllAuditProfileLogEntries() throws Exception {
        // Initialize the database
        auditProfileLogEntryRepository.saveAndFlush(auditProfileLogEntry);

        // Get all the auditProfileLogEntries
        restAuditProfileLogEntryMockMvc.perform(get("/api/audit-profile-log-entries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auditProfileLogEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].happened").value(hasItem(DEFAULT_HAPPENED_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAuditProfileLogEntry() throws Exception {
        // Initialize the database
        auditProfileLogEntryRepository.saveAndFlush(auditProfileLogEntry);

        // Get the auditProfileLogEntry
        restAuditProfileLogEntryMockMvc.perform(get("/api/audit-profile-log-entries/{id}", auditProfileLogEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auditProfileLogEntry.getId().intValue()))
            .andExpect(jsonPath("$.happened").value(DEFAULT_HAPPENED_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuditProfileLogEntry() throws Exception {
        // Get the auditProfileLogEntry
        restAuditProfileLogEntryMockMvc.perform(get("/api/audit-profile-log-entries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditProfileLogEntry() throws Exception {
        // Initialize the database
        auditProfileLogEntryRepository.saveAndFlush(auditProfileLogEntry);
        auditProfileLogEntrySearchRepository.save(auditProfileLogEntry);
        int databaseSizeBeforeUpdate = auditProfileLogEntryRepository.findAll().size();

        // Update the auditProfileLogEntry
        AuditProfileLogEntry updatedAuditProfileLogEntry = auditProfileLogEntryRepository.findOne(auditProfileLogEntry.getId());
        updatedAuditProfileLogEntry
                .happened(UPDATED_HAPPENED)
                .description(UPDATED_DESCRIPTION);

        restAuditProfileLogEntryMockMvc.perform(put("/api/audit-profile-log-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuditProfileLogEntry)))
                .andExpect(status().isOk());

        // Validate the AuditProfileLogEntry in the database
        List<AuditProfileLogEntry> auditProfileLogEntries = auditProfileLogEntryRepository.findAll();
        assertThat(auditProfileLogEntries).hasSize(databaseSizeBeforeUpdate);
        AuditProfileLogEntry testAuditProfileLogEntry = auditProfileLogEntries.get(auditProfileLogEntries.size() - 1);
        assertThat(testAuditProfileLogEntry.getHappened()).isEqualTo(UPDATED_HAPPENED);
        assertThat(testAuditProfileLogEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the AuditProfileLogEntry in ElasticSearch
        AuditProfileLogEntry auditProfileLogEntryEs = auditProfileLogEntrySearchRepository.findOne(testAuditProfileLogEntry.getId());
        assertThat(auditProfileLogEntryEs).isEqualToComparingFieldByField(testAuditProfileLogEntry);
    }

    @Test
    @Transactional
    public void deleteAuditProfileLogEntry() throws Exception {
        // Initialize the database
        auditProfileLogEntryRepository.saveAndFlush(auditProfileLogEntry);
        auditProfileLogEntrySearchRepository.save(auditProfileLogEntry);
        int databaseSizeBeforeDelete = auditProfileLogEntryRepository.findAll().size();

        // Get the auditProfileLogEntry
        restAuditProfileLogEntryMockMvc.perform(delete("/api/audit-profile-log-entries/{id}", auditProfileLogEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean auditProfileLogEntryExistsInEs = auditProfileLogEntrySearchRepository.exists(auditProfileLogEntry.getId());
        assertThat(auditProfileLogEntryExistsInEs).isFalse();

        // Validate the database is empty
        List<AuditProfileLogEntry> auditProfileLogEntries = auditProfileLogEntryRepository.findAll();
        assertThat(auditProfileLogEntries).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAuditProfileLogEntry() throws Exception {
        // Initialize the database
        auditProfileLogEntryRepository.saveAndFlush(auditProfileLogEntry);
        auditProfileLogEntrySearchRepository.save(auditProfileLogEntry);

        // Search the auditProfileLogEntry
        restAuditProfileLogEntryMockMvc.perform(get("/api/_search/audit-profile-log-entries?query=id:" + auditProfileLogEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditProfileLogEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].happened").value(hasItem(DEFAULT_HAPPENED_STR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
