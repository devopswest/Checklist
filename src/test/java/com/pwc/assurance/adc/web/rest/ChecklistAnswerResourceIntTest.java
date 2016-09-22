package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ChecklistAnswer;
import com.pwc.assurance.adc.repository.ChecklistAnswerRepository;
import com.pwc.assurance.adc.service.ChecklistAnswerService;
import com.pwc.assurance.adc.repository.search.ChecklistAnswerSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistAnswerDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistAnswerMapper;

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
 * Test class for the ChecklistAnswerResource REST controller.
 *
 * @see ChecklistAnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ChecklistAnswerResourceIntTest {
    private static final String DEFAULT_ANSWER = "AAAAA";
    private static final String UPDATED_ANSWER = "BBBBB";
    private static final String DEFAULT_COMMENTS = "AAAAA";
    private static final String UPDATED_COMMENTS = "BBBBB";

    @Inject
    private ChecklistAnswerRepository checklistAnswerRepository;

    @Inject
    private ChecklistAnswerMapper checklistAnswerMapper;

    @Inject
    private ChecklistAnswerService checklistAnswerService;

    @Inject
    private ChecklistAnswerSearchRepository checklistAnswerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChecklistAnswerMockMvc;

    private ChecklistAnswer checklistAnswer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChecklistAnswerResource checklistAnswerResource = new ChecklistAnswerResource();
        ReflectionTestUtils.setField(checklistAnswerResource, "checklistAnswerService", checklistAnswerService);
        this.restChecklistAnswerMockMvc = MockMvcBuilders.standaloneSetup(checklistAnswerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChecklistAnswer createEntity(EntityManager em) {
        ChecklistAnswer checklistAnswer = new ChecklistAnswer();
        checklistAnswer = new ChecklistAnswer()
                .answer(DEFAULT_ANSWER)
                .comments(DEFAULT_COMMENTS);
        return checklistAnswer;
    }

    @Before
    public void initTest() {
        checklistAnswerSearchRepository.deleteAll();
        checklistAnswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createChecklistAnswer() throws Exception {
        int databaseSizeBeforeCreate = checklistAnswerRepository.findAll().size();

        // Create the ChecklistAnswer
        ChecklistAnswerDTO checklistAnswerDTO = checklistAnswerMapper.checklistAnswerToChecklistAnswerDTO(checklistAnswer);

        restChecklistAnswerMockMvc.perform(post("/api/checklist-answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistAnswerDTO)))
                .andExpect(status().isCreated());

        // Validate the ChecklistAnswer in the database
        List<ChecklistAnswer> checklistAnswers = checklistAnswerRepository.findAll();
        assertThat(checklistAnswers).hasSize(databaseSizeBeforeCreate + 1);
        ChecklistAnswer testChecklistAnswer = checklistAnswers.get(checklistAnswers.size() - 1);
        assertThat(testChecklistAnswer.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testChecklistAnswer.getComments()).isEqualTo(DEFAULT_COMMENTS);

        // Validate the ChecklistAnswer in ElasticSearch
        ChecklistAnswer checklistAnswerEs = checklistAnswerSearchRepository.findOne(testChecklistAnswer.getId());
        assertThat(checklistAnswerEs).isEqualToComparingFieldByField(testChecklistAnswer);
    }

    @Test
    @Transactional
    public void getAllChecklistAnswers() throws Exception {
        // Initialize the database
        checklistAnswerRepository.saveAndFlush(checklistAnswer);

        // Get all the checklistAnswers
        restChecklistAnswerMockMvc.perform(get("/api/checklist-answers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(checklistAnswer.getId().intValue())))
                .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
                .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void getChecklistAnswer() throws Exception {
        // Initialize the database
        checklistAnswerRepository.saveAndFlush(checklistAnswer);

        // Get the checklistAnswer
        restChecklistAnswerMockMvc.perform(get("/api/checklist-answers/{id}", checklistAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checklistAnswer.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChecklistAnswer() throws Exception {
        // Get the checklistAnswer
        restChecklistAnswerMockMvc.perform(get("/api/checklist-answers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChecklistAnswer() throws Exception {
        // Initialize the database
        checklistAnswerRepository.saveAndFlush(checklistAnswer);
        checklistAnswerSearchRepository.save(checklistAnswer);
        int databaseSizeBeforeUpdate = checklistAnswerRepository.findAll().size();

        // Update the checklistAnswer
        ChecklistAnswer updatedChecklistAnswer = checklistAnswerRepository.findOne(checklistAnswer.getId());
        updatedChecklistAnswer
                .answer(UPDATED_ANSWER)
                .comments(UPDATED_COMMENTS);
        ChecklistAnswerDTO checklistAnswerDTO = checklistAnswerMapper.checklistAnswerToChecklistAnswerDTO(updatedChecklistAnswer);

        restChecklistAnswerMockMvc.perform(put("/api/checklist-answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistAnswerDTO)))
                .andExpect(status().isOk());

        // Validate the ChecklistAnswer in the database
        List<ChecklistAnswer> checklistAnswers = checklistAnswerRepository.findAll();
        assertThat(checklistAnswers).hasSize(databaseSizeBeforeUpdate);
        ChecklistAnswer testChecklistAnswer = checklistAnswers.get(checklistAnswers.size() - 1);
        assertThat(testChecklistAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testChecklistAnswer.getComments()).isEqualTo(UPDATED_COMMENTS);

        // Validate the ChecklistAnswer in ElasticSearch
        ChecklistAnswer checklistAnswerEs = checklistAnswerSearchRepository.findOne(testChecklistAnswer.getId());
        assertThat(checklistAnswerEs).isEqualToComparingFieldByField(testChecklistAnswer);
    }

    @Test
    @Transactional
    public void deleteChecklistAnswer() throws Exception {
        // Initialize the database
        checklistAnswerRepository.saveAndFlush(checklistAnswer);
        checklistAnswerSearchRepository.save(checklistAnswer);
        int databaseSizeBeforeDelete = checklistAnswerRepository.findAll().size();

        // Get the checklistAnswer
        restChecklistAnswerMockMvc.perform(delete("/api/checklist-answers/{id}", checklistAnswer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean checklistAnswerExistsInEs = checklistAnswerSearchRepository.exists(checklistAnswer.getId());
        assertThat(checklistAnswerExistsInEs).isFalse();

        // Validate the database is empty
        List<ChecklistAnswer> checklistAnswers = checklistAnswerRepository.findAll();
        assertThat(checklistAnswers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChecklistAnswer() throws Exception {
        // Initialize the database
        checklistAnswerRepository.saveAndFlush(checklistAnswer);
        checklistAnswerSearchRepository.save(checklistAnswer);

        // Search the checklistAnswer
        restChecklistAnswerMockMvc.perform(get("/api/_search/checklist-answers?query=id:" + checklistAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checklistAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }
}
