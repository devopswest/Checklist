package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.NotificationAction;
import com.pwc.assurance.adc.repository.NotificationActionRepository;
import com.pwc.assurance.adc.service.NotificationActionService;
import com.pwc.assurance.adc.repository.search.NotificationActionSearchRepository;
import com.pwc.assurance.adc.service.dto.NotificationActionDTO;
import com.pwc.assurance.adc.service.mapper.NotificationActionMapper;

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
 * Test class for the NotificationActionResource REST controller.
 *
 * @see NotificationActionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class NotificationActionResourceIntTest {
    private static final String DEFAULT_ACTION = "AAAAA";
    private static final String UPDATED_ACTION = "BBBBB";

    @Inject
    private NotificationActionRepository notificationActionRepository;

    @Inject
    private NotificationActionMapper notificationActionMapper;

    @Inject
    private NotificationActionService notificationActionService;

    @Inject
    private NotificationActionSearchRepository notificationActionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restNotificationActionMockMvc;

    private NotificationAction notificationAction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationActionResource notificationActionResource = new NotificationActionResource();
        ReflectionTestUtils.setField(notificationActionResource, "notificationActionService", notificationActionService);
        this.restNotificationActionMockMvc = MockMvcBuilders.standaloneSetup(notificationActionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationAction createEntity(EntityManager em) {
        NotificationAction notificationAction = new NotificationAction();
        notificationAction = new NotificationAction()
                .action(DEFAULT_ACTION);
        return notificationAction;
    }

    @Before
    public void initTest() {
        notificationActionSearchRepository.deleteAll();
        notificationAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationAction() throws Exception {
        int databaseSizeBeforeCreate = notificationActionRepository.findAll().size();

        // Create the NotificationAction
        NotificationActionDTO notificationActionDTO = notificationActionMapper.notificationActionToNotificationActionDTO(notificationAction);

        restNotificationActionMockMvc.perform(post("/api/notification-actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationActionDTO)))
                .andExpect(status().isCreated());

        // Validate the NotificationAction in the database
        List<NotificationAction> notificationActions = notificationActionRepository.findAll();
        assertThat(notificationActions).hasSize(databaseSizeBeforeCreate + 1);
        NotificationAction testNotificationAction = notificationActions.get(notificationActions.size() - 1);
        assertThat(testNotificationAction.getAction()).isEqualTo(DEFAULT_ACTION);

        // Validate the NotificationAction in ElasticSearch
        NotificationAction notificationActionEs = notificationActionSearchRepository.findOne(testNotificationAction.getId());
        assertThat(notificationActionEs).isEqualToComparingFieldByField(testNotificationAction);
    }

    @Test
    @Transactional
    public void getAllNotificationActions() throws Exception {
        // Initialize the database
        notificationActionRepository.saveAndFlush(notificationAction);

        // Get all the notificationActions
        restNotificationActionMockMvc.perform(get("/api/notification-actions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notificationAction.getId().intValue())))
                .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }

    @Test
    @Transactional
    public void getNotificationAction() throws Exception {
        // Initialize the database
        notificationActionRepository.saveAndFlush(notificationAction);

        // Get the notificationAction
        restNotificationActionMockMvc.perform(get("/api/notification-actions/{id}", notificationAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificationAction.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationAction() throws Exception {
        // Get the notificationAction
        restNotificationActionMockMvc.perform(get("/api/notification-actions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationAction() throws Exception {
        // Initialize the database
        notificationActionRepository.saveAndFlush(notificationAction);
        notificationActionSearchRepository.save(notificationAction);
        int databaseSizeBeforeUpdate = notificationActionRepository.findAll().size();

        // Update the notificationAction
        NotificationAction updatedNotificationAction = notificationActionRepository.findOne(notificationAction.getId());
        updatedNotificationAction
                .action(UPDATED_ACTION);
        NotificationActionDTO notificationActionDTO = notificationActionMapper.notificationActionToNotificationActionDTO(updatedNotificationAction);

        restNotificationActionMockMvc.perform(put("/api/notification-actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationActionDTO)))
                .andExpect(status().isOk());

        // Validate the NotificationAction in the database
        List<NotificationAction> notificationActions = notificationActionRepository.findAll();
        assertThat(notificationActions).hasSize(databaseSizeBeforeUpdate);
        NotificationAction testNotificationAction = notificationActions.get(notificationActions.size() - 1);
        assertThat(testNotificationAction.getAction()).isEqualTo(UPDATED_ACTION);

        // Validate the NotificationAction in ElasticSearch
        NotificationAction notificationActionEs = notificationActionSearchRepository.findOne(testNotificationAction.getId());
        assertThat(notificationActionEs).isEqualToComparingFieldByField(testNotificationAction);
    }

    @Test
    @Transactional
    public void deleteNotificationAction() throws Exception {
        // Initialize the database
        notificationActionRepository.saveAndFlush(notificationAction);
        notificationActionSearchRepository.save(notificationAction);
        int databaseSizeBeforeDelete = notificationActionRepository.findAll().size();

        // Get the notificationAction
        restNotificationActionMockMvc.perform(delete("/api/notification-actions/{id}", notificationAction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean notificationActionExistsInEs = notificationActionSearchRepository.exists(notificationAction.getId());
        assertThat(notificationActionExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationAction> notificationActions = notificationActionRepository.findAll();
        assertThat(notificationActions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationAction() throws Exception {
        // Initialize the database
        notificationActionRepository.saveAndFlush(notificationAction);
        notificationActionSearchRepository.save(notificationAction);

        // Search the notificationAction
        restNotificationActionMockMvc.perform(get("/api/_search/notification-actions?query=id:" + notificationAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }
}
