package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.EngagementMember;
import com.pwc.assurance.adc.repository.EngagementMemberRepository;
import com.pwc.assurance.adc.repository.search.EngagementMemberSearchRepository;

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

import com.pwc.assurance.adc.domain.enumeration.EngagementAuthorities;
/**
 * Test class for the EngagementMemberResource REST controller.
 *
 * @see EngagementMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class EngagementMemberResourceIntTest {
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final EngagementAuthorities DEFAULT_AUTHORITY = EngagementAuthorities.ROLE_PWC_ENGAGEMENT_TEAM;
    private static final EngagementAuthorities UPDATED_AUTHORITY = EngagementAuthorities.ROLE_PWC_PERSSONEL_US;

    @Inject
    private EngagementMemberRepository engagementMemberRepository;

    @Inject
    private EngagementMemberSearchRepository engagementMemberSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEngagementMemberMockMvc;

    private EngagementMember engagementMember;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EngagementMemberResource engagementMemberResource = new EngagementMemberResource();
        ReflectionTestUtils.setField(engagementMemberResource, "engagementMemberSearchRepository", engagementMemberSearchRepository);
        ReflectionTestUtils.setField(engagementMemberResource, "engagementMemberRepository", engagementMemberRepository);
        this.restEngagementMemberMockMvc = MockMvcBuilders.standaloneSetup(engagementMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EngagementMember createEntity(EntityManager em) {
        EngagementMember engagementMember = new EngagementMember();
        engagementMember = new EngagementMember()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .phone(DEFAULT_PHONE)
                .email(DEFAULT_EMAIL)
                .authority(DEFAULT_AUTHORITY);
        return engagementMember;
    }

    @Before
    public void initTest() {
        engagementMemberSearchRepository.deleteAll();
        engagementMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createEngagementMember() throws Exception {
        int databaseSizeBeforeCreate = engagementMemberRepository.findAll().size();

        // Create the EngagementMember

        restEngagementMemberMockMvc.perform(post("/api/engagement-members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engagementMember)))
                .andExpect(status().isCreated());

        // Validate the EngagementMember in the database
        List<EngagementMember> engagementMembers = engagementMemberRepository.findAll();
        assertThat(engagementMembers).hasSize(databaseSizeBeforeCreate + 1);
        EngagementMember testEngagementMember = engagementMembers.get(engagementMembers.size() - 1);
        assertThat(testEngagementMember.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEngagementMember.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEngagementMember.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testEngagementMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEngagementMember.getAuthority()).isEqualTo(DEFAULT_AUTHORITY);

        // Validate the EngagementMember in ElasticSearch
        EngagementMember engagementMemberEs = engagementMemberSearchRepository.findOne(testEngagementMember.getId());
        assertThat(engagementMemberEs).isEqualToComparingFieldByField(testEngagementMember);
    }

    @Test
    @Transactional
    public void getAllEngagementMembers() throws Exception {
        // Initialize the database
        engagementMemberRepository.saveAndFlush(engagementMember);

        // Get all the engagementMembers
        restEngagementMemberMockMvc.perform(get("/api/engagement-members?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(engagementMember.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));
    }

    @Test
    @Transactional
    public void getEngagementMember() throws Exception {
        // Initialize the database
        engagementMemberRepository.saveAndFlush(engagementMember);

        // Get the engagementMember
        restEngagementMemberMockMvc.perform(get("/api/engagement-members/{id}", engagementMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(engagementMember.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.authority").value(DEFAULT_AUTHORITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEngagementMember() throws Exception {
        // Get the engagementMember
        restEngagementMemberMockMvc.perform(get("/api/engagement-members/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEngagementMember() throws Exception {
        // Initialize the database
        engagementMemberRepository.saveAndFlush(engagementMember);
        engagementMemberSearchRepository.save(engagementMember);
        int databaseSizeBeforeUpdate = engagementMemberRepository.findAll().size();

        // Update the engagementMember
        EngagementMember updatedEngagementMember = engagementMemberRepository.findOne(engagementMember.getId());
        updatedEngagementMember
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .phone(UPDATED_PHONE)
                .email(UPDATED_EMAIL)
                .authority(UPDATED_AUTHORITY);

        restEngagementMemberMockMvc.perform(put("/api/engagement-members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEngagementMember)))
                .andExpect(status().isOk());

        // Validate the EngagementMember in the database
        List<EngagementMember> engagementMembers = engagementMemberRepository.findAll();
        assertThat(engagementMembers).hasSize(databaseSizeBeforeUpdate);
        EngagementMember testEngagementMember = engagementMembers.get(engagementMembers.size() - 1);
        assertThat(testEngagementMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEngagementMember.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEngagementMember.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEngagementMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEngagementMember.getAuthority()).isEqualTo(UPDATED_AUTHORITY);

        // Validate the EngagementMember in ElasticSearch
        EngagementMember engagementMemberEs = engagementMemberSearchRepository.findOne(testEngagementMember.getId());
        assertThat(engagementMemberEs).isEqualToComparingFieldByField(testEngagementMember);
    }

    @Test
    @Transactional
    public void deleteEngagementMember() throws Exception {
        // Initialize the database
        engagementMemberRepository.saveAndFlush(engagementMember);
        engagementMemberSearchRepository.save(engagementMember);
        int databaseSizeBeforeDelete = engagementMemberRepository.findAll().size();

        // Get the engagementMember
        restEngagementMemberMockMvc.perform(delete("/api/engagement-members/{id}", engagementMember.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean engagementMemberExistsInEs = engagementMemberSearchRepository.exists(engagementMember.getId());
        assertThat(engagementMemberExistsInEs).isFalse();

        // Validate the database is empty
        List<EngagementMember> engagementMembers = engagementMemberRepository.findAll();
        assertThat(engagementMembers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEngagementMember() throws Exception {
        // Initialize the database
        engagementMemberRepository.saveAndFlush(engagementMember);
        engagementMemberSearchRepository.save(engagementMember);

        // Search the engagementMember
        restEngagementMemberMockMvc.perform(get("/api/_search/engagement-members?query=id:" + engagementMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engagementMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));
    }
}
