package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.UserProfile;
import com.pwc.assurance.adc.repository.UserProfileRepository;
import com.pwc.assurance.adc.repository.search.UserProfileSearchRepository;

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
 * Test class for the UserProfileResource REST controller.
 *
 * @see UserProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class UserProfileResourceIntTest {
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private UserProfileRepository userProfileRepository;

    @Inject
    private UserProfileSearchRepository userProfileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserProfileResource userProfileResource = new UserProfileResource();
        ReflectionTestUtils.setField(userProfileResource, "userProfileSearchRepository", userProfileSearchRepository);
        ReflectionTestUtils.setField(userProfileResource, "userProfileRepository", userProfileRepository);
        this.restUserProfileMockMvc = MockMvcBuilders.standaloneSetup(userProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile();
        userProfile = new UserProfile();
        userProfile.setFirstName(DEFAULT_FIRST_NAME);
        userProfile.setLastName(DEFAULT_LAST_NAME);
        userProfile.setPhone(DEFAULT_PHONE);
        userProfile.setEmail(DEFAULT_EMAIL);
        return userProfile;
    }

    @Before
    public void initTest() {
        userProfileSearchRepository.deleteAll();
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile

        restUserProfileMockMvc.perform(post("/api/user-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userProfile)))
                .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfiles.get(userProfiles.size() - 1);
        assertThat(testUserProfile.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserProfile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the UserProfile in ElasticSearch
        UserProfile userProfileEs = userProfileSearchRepository.findOne(testUserProfile.getId());
        assertThat(userProfileEs).isEqualToComparingFieldByField(testUserProfile);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfiles
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        userProfileSearchRepository.save(userProfile);
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findOne(userProfile.getId());
        updatedUserProfile.setFirstName(UPDATED_FIRST_NAME);
        updatedUserProfile.setLastName(UPDATED_LAST_NAME);
        updatedUserProfile.setPhone(UPDATED_PHONE);
        updatedUserProfile.setEmail(UPDATED_EMAIL);

        restUserProfileMockMvc.perform(put("/api/user-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserProfile)))
                .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfiles.get(userProfiles.size() - 1);
        assertThat(testUserProfile.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserProfile.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the UserProfile in ElasticSearch
        UserProfile userProfileEs = userProfileSearchRepository.findOne(testUserProfile.getId());
        assertThat(userProfileEs).isEqualToComparingFieldByField(testUserProfile);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        userProfileSearchRepository.save(userProfile);
        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Get the userProfile
        restUserProfileMockMvc.perform(delete("/api/user-profiles/{id}", userProfile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean userProfileExistsInEs = userProfileSearchRepository.exists(userProfile.getId());
        assertThat(userProfileExistsInEs).isFalse();

        // Validate the database is empty
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        userProfileSearchRepository.save(userProfile);

        // Search the userProfile
        restUserProfileMockMvc.perform(get("/api/_search/user-profiles?query=id:" + userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
}
