package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ChecklistQuestion;
import com.pwc.assurance.adc.repository.ChecklistQuestionRepository;
import com.pwc.assurance.adc.repository.search.ChecklistQuestionSearchRepository;
import com.pwc.assurance.adc.service.dto.ChecklistQuestionDTO;
import com.pwc.assurance.adc.service.mapper.ChecklistQuestionMapper;

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
 * Test class for the ChecklistQuestionResource REST controller.
 *
 * @see ChecklistQuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ChecklistQuestionResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ChecklistQuestionRepository checklistQuestionRepository;

    @Inject
    private ChecklistQuestionMapper checklistQuestionMapper;

    @Inject
    private ChecklistQuestionSearchRepository checklistQuestionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChecklistQuestionMockMvc;

    private ChecklistQuestion checklistQuestion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChecklistQuestionResource checklistQuestionResource = new ChecklistQuestionResource();
        ReflectionTestUtils.setField(checklistQuestionResource, "checklistQuestionSearchRepository", checklistQuestionSearchRepository);
        ReflectionTestUtils.setField(checklistQuestionResource, "checklistQuestionRepository", checklistQuestionRepository);
        ReflectionTestUtils.setField(checklistQuestionResource, "checklistQuestionMapper", checklistQuestionMapper);
        this.restChecklistQuestionMockMvc = MockMvcBuilders.standaloneSetup(checklistQuestionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChecklistQuestion createEntity(EntityManager em) {
        ChecklistQuestion checklistQuestion = new ChecklistQuestion();
        checklistQuestion = new ChecklistQuestion()
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION);
        return checklistQuestion;
    }

    @Before
    public void initTest() {
        checklistQuestionSearchRepository.deleteAll();
        checklistQuestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createChecklistQuestion() throws Exception {
        int databaseSizeBeforeCreate = checklistQuestionRepository.findAll().size();

        // Create the ChecklistQuestion
        ChecklistQuestionDTO checklistQuestionDTO = checklistQuestionMapper.checklistQuestionToChecklistQuestionDTO(checklistQuestion);

        restChecklistQuestionMockMvc.perform(post("/api/checklist-questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistQuestionDTO)))
                .andExpect(status().isCreated());

        // Validate the ChecklistQuestion in the database
        List<ChecklistQuestion> checklistQuestions = checklistQuestionRepository.findAll();
        assertThat(checklistQuestions).hasSize(databaseSizeBeforeCreate + 1);
        ChecklistQuestion testChecklistQuestion = checklistQuestions.get(checklistQuestions.size() - 1);
        assertThat(testChecklistQuestion.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChecklistQuestion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ChecklistQuestion in ElasticSearch
        ChecklistQuestion checklistQuestionEs = checklistQuestionSearchRepository.findOne(testChecklistQuestion.getId());
        assertThat(checklistQuestionEs).isEqualToComparingFieldByField(testChecklistQuestion);
    }

    @Test
    @Transactional
    public void getAllChecklistQuestions() throws Exception {
        // Initialize the database
        checklistQuestionRepository.saveAndFlush(checklistQuestion);

        // Get all the checklistQuestions
        restChecklistQuestionMockMvc.perform(get("/api/checklist-questions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(checklistQuestion.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getChecklistQuestion() throws Exception {
        // Initialize the database
        checklistQuestionRepository.saveAndFlush(checklistQuestion);

        // Get the checklistQuestion
        restChecklistQuestionMockMvc.perform(get("/api/checklist-questions/{id}", checklistQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checklistQuestion.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChecklistQuestion() throws Exception {
        // Get the checklistQuestion
        restChecklistQuestionMockMvc.perform(get("/api/checklist-questions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChecklistQuestion() throws Exception {
        // Initialize the database
        checklistQuestionRepository.saveAndFlush(checklistQuestion);
        checklistQuestionSearchRepository.save(checklistQuestion);
        int databaseSizeBeforeUpdate = checklistQuestionRepository.findAll().size();

        // Update the checklistQuestion
        ChecklistQuestion updatedChecklistQuestion = checklistQuestionRepository.findOne(checklistQuestion.getId());
        updatedChecklistQuestion
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION);
        ChecklistQuestionDTO checklistQuestionDTO = checklistQuestionMapper.checklistQuestionToChecklistQuestionDTO(updatedChecklistQuestion);

        restChecklistQuestionMockMvc.perform(put("/api/checklist-questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checklistQuestionDTO)))
                .andExpect(status().isOk());

        // Validate the ChecklistQuestion in the database
        List<ChecklistQuestion> checklistQuestions = checklistQuestionRepository.findAll();
        assertThat(checklistQuestions).hasSize(databaseSizeBeforeUpdate);
        ChecklistQuestion testChecklistQuestion = checklistQuestions.get(checklistQuestions.size() - 1);
        assertThat(testChecklistQuestion.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChecklistQuestion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ChecklistQuestion in ElasticSearch
        ChecklistQuestion checklistQuestionEs = checklistQuestionSearchRepository.findOne(testChecklistQuestion.getId());
        assertThat(checklistQuestionEs).isEqualToComparingFieldByField(testChecklistQuestion);
    }

    @Test
    @Transactional
    public void deleteChecklistQuestion() throws Exception {
        // Initialize the database
        checklistQuestionRepository.saveAndFlush(checklistQuestion);
        checklistQuestionSearchRepository.save(checklistQuestion);
        int databaseSizeBeforeDelete = checklistQuestionRepository.findAll().size();

        // Get the checklistQuestion
        restChecklistQuestionMockMvc.perform(delete("/api/checklist-questions/{id}", checklistQuestion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean checklistQuestionExistsInEs = checklistQuestionSearchRepository.exists(checklistQuestion.getId());
        assertThat(checklistQuestionExistsInEs).isFalse();

        // Validate the database is empty
        List<ChecklistQuestion> checklistQuestions = checklistQuestionRepository.findAll();
        assertThat(checklistQuestions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChecklistQuestion() throws Exception {
        // Initialize the database
        checklistQuestionRepository.saveAndFlush(checklistQuestion);
        checklistQuestionSearchRepository.save(checklistQuestion);

        // Search the checklistQuestion
        restChecklistQuestionMockMvc.perform(get("/api/_search/checklist-questions?query=id:" + checklistQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checklistQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
