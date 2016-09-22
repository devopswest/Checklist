package com.pwc.assurance.adc.web.rest;

import com.pwc.assurance.adc.ChecklistApp;
import com.pwc.assurance.adc.domain.ClientTag;
import com.pwc.assurance.adc.repository.ClientTagRepository;
import com.pwc.assurance.adc.service.ClientTagService;
import com.pwc.assurance.adc.repository.search.ClientTagSearchRepository;
import com.pwc.assurance.adc.service.dto.ClientTagDTO;
import com.pwc.assurance.adc.service.mapper.ClientTagMapper;

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
 * Test class for the ClientTagResource REST controller.
 *
 * @see ClientTagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChecklistApp.class)
public class ClientTagResourceIntTest {
    private static final String DEFAULT_TAG_VALUE = "AAAAA";
    private static final String UPDATED_TAG_VALUE = "BBBBB";

    @Inject
    private ClientTagRepository clientTagRepository;

    @Inject
    private ClientTagMapper clientTagMapper;

    @Inject
    private ClientTagService clientTagService;

    @Inject
    private ClientTagSearchRepository clientTagSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClientTagMockMvc;

    private ClientTag clientTag;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientTagResource clientTagResource = new ClientTagResource();
        ReflectionTestUtils.setField(clientTagResource, "clientTagService", clientTagService);
        this.restClientTagMockMvc = MockMvcBuilders.standaloneSetup(clientTagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientTag createEntity(EntityManager em) {
        ClientTag clientTag = new ClientTag();
        clientTag = new ClientTag()
                .tagValue(DEFAULT_TAG_VALUE);
        return clientTag;
    }

    @Before
    public void initTest() {
        clientTagSearchRepository.deleteAll();
        clientTag = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientTag() throws Exception {
        int databaseSizeBeforeCreate = clientTagRepository.findAll().size();

        // Create the ClientTag
        ClientTagDTO clientTagDTO = clientTagMapper.clientTagToClientTagDTO(clientTag);

        restClientTagMockMvc.perform(post("/api/client-tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientTagDTO)))
                .andExpect(status().isCreated());

        // Validate the ClientTag in the database
        List<ClientTag> clientTags = clientTagRepository.findAll();
        assertThat(clientTags).hasSize(databaseSizeBeforeCreate + 1);
        ClientTag testClientTag = clientTags.get(clientTags.size() - 1);
        assertThat(testClientTag.getTagValue()).isEqualTo(DEFAULT_TAG_VALUE);

        // Validate the ClientTag in ElasticSearch
        ClientTag clientTagEs = clientTagSearchRepository.findOne(testClientTag.getId());
        assertThat(clientTagEs).isEqualToComparingFieldByField(testClientTag);
    }

    @Test
    @Transactional
    public void getAllClientTags() throws Exception {
        // Initialize the database
        clientTagRepository.saveAndFlush(clientTag);

        // Get all the clientTags
        restClientTagMockMvc.perform(get("/api/client-tags?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientTag.getId().intValue())))
                .andExpect(jsonPath("$.[*].tagValue").value(hasItem(DEFAULT_TAG_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getClientTag() throws Exception {
        // Initialize the database
        clientTagRepository.saveAndFlush(clientTag);

        // Get the clientTag
        restClientTagMockMvc.perform(get("/api/client-tags/{id}", clientTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientTag.getId().intValue()))
            .andExpect(jsonPath("$.tagValue").value(DEFAULT_TAG_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClientTag() throws Exception {
        // Get the clientTag
        restClientTagMockMvc.perform(get("/api/client-tags/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientTag() throws Exception {
        // Initialize the database
        clientTagRepository.saveAndFlush(clientTag);
        clientTagSearchRepository.save(clientTag);
        int databaseSizeBeforeUpdate = clientTagRepository.findAll().size();

        // Update the clientTag
        ClientTag updatedClientTag = clientTagRepository.findOne(clientTag.getId());
        updatedClientTag
                .tagValue(UPDATED_TAG_VALUE);
        ClientTagDTO clientTagDTO = clientTagMapper.clientTagToClientTagDTO(updatedClientTag);

        restClientTagMockMvc.perform(put("/api/client-tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientTagDTO)))
                .andExpect(status().isOk());

        // Validate the ClientTag in the database
        List<ClientTag> clientTags = clientTagRepository.findAll();
        assertThat(clientTags).hasSize(databaseSizeBeforeUpdate);
        ClientTag testClientTag = clientTags.get(clientTags.size() - 1);
        assertThat(testClientTag.getTagValue()).isEqualTo(UPDATED_TAG_VALUE);

        // Validate the ClientTag in ElasticSearch
        ClientTag clientTagEs = clientTagSearchRepository.findOne(testClientTag.getId());
        assertThat(clientTagEs).isEqualToComparingFieldByField(testClientTag);
    }

    @Test
    @Transactional
    public void deleteClientTag() throws Exception {
        // Initialize the database
        clientTagRepository.saveAndFlush(clientTag);
        clientTagSearchRepository.save(clientTag);
        int databaseSizeBeforeDelete = clientTagRepository.findAll().size();

        // Get the clientTag
        restClientTagMockMvc.perform(delete("/api/client-tags/{id}", clientTag.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientTagExistsInEs = clientTagSearchRepository.exists(clientTag.getId());
        assertThat(clientTagExistsInEs).isFalse();

        // Validate the database is empty
        List<ClientTag> clientTags = clientTagRepository.findAll();
        assertThat(clientTags).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClientTag() throws Exception {
        // Initialize the database
        clientTagRepository.saveAndFlush(clientTag);
        clientTagSearchRepository.save(clientTag);

        // Search the clientTag
        restClientTagMockMvc.perform(get("/api/_search/client-tags?query=id:" + clientTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagValue").value(hasItem(DEFAULT_TAG_VALUE.toString())));
    }
}
