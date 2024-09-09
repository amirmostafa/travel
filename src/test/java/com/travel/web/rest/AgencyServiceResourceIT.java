package com.travel.web.rest;

import static com.travel.domain.AgencyServiceAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.AgencyService;
import com.travel.repository.AgencyServiceRepository;
import com.travel.service.dto.AgencyServiceDTO;
import com.travel.service.mapper.AgencyServiceMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AgencyServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgencyServiceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agency-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgencyServiceRepository agencyServiceRepository;

    @Autowired
    private AgencyServiceMapper agencyServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyServiceMockMvc;

    private AgencyService agencyService;

    private AgencyService insertedAgencyService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyService createEntity(EntityManager em) {
        AgencyService agencyService = new AgencyService().title(DEFAULT_TITLE).icon(DEFAULT_ICON).content(DEFAULT_CONTENT);
        return agencyService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyService createUpdatedEntity(EntityManager em) {
        AgencyService agencyService = new AgencyService().title(UPDATED_TITLE).icon(UPDATED_ICON).content(UPDATED_CONTENT);
        return agencyService;
    }

    @BeforeEach
    public void initTest() {
        agencyService = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgencyService != null) {
            agencyServiceRepository.delete(insertedAgencyService);
            insertedAgencyService = null;
        }
    }

    @Test
    @Transactional
    void createAgencyService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgencyService
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);
        var returnedAgencyServiceDTO = om.readValue(
            restAgencyServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgencyServiceDTO.class
        );

        // Validate the AgencyService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAgencyService = agencyServiceMapper.toEntity(returnedAgencyServiceDTO);
        assertAgencyServiceUpdatableFieldsEquals(returnedAgencyService, getPersistedAgencyService(returnedAgencyService));

        insertedAgencyService = returnedAgencyService;
    }

    @Test
    @Transactional
    void createAgencyServiceWithExistingId() throws Exception {
        // Create the AgencyService with an existing ID
        agencyService.setId(1L);
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agencyService.setTitle(null);

        // Create the AgencyService, which fails.
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        restAgencyServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agencyService.setContent(null);

        // Create the AgencyService, which fails.
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        restAgencyServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgencyServices() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList
        restAgencyServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyService.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getAgencyService() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get the agencyService
        restAgencyServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, agencyService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agencyService.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getAgencyServicesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        Long id = agencyService.getId();

        defaultAgencyServiceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAgencyServiceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAgencyServiceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where title equals to
        defaultAgencyServiceFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where title in
        defaultAgencyServiceFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where title is not null
        defaultAgencyServiceFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyServicesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where title contains
        defaultAgencyServiceFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where title does not contain
        defaultAgencyServiceFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where icon equals to
        defaultAgencyServiceFiltering("icon.equals=" + DEFAULT_ICON, "icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByIconIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where icon in
        defaultAgencyServiceFiltering("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON, "icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where icon is not null
        defaultAgencyServiceFiltering("icon.specified=true", "icon.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyServicesByIconContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where icon contains
        defaultAgencyServiceFiltering("icon.contains=" + DEFAULT_ICON, "icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByIconNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where icon does not contain
        defaultAgencyServiceFiltering("icon.doesNotContain=" + UPDATED_ICON, "icon.doesNotContain=" + DEFAULT_ICON);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where content equals to
        defaultAgencyServiceFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where content in
        defaultAgencyServiceFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where content is not null
        defaultAgencyServiceFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyServicesByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where content contains
        defaultAgencyServiceFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllAgencyServicesByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        // Get all the agencyServiceList where content does not contain
        defaultAgencyServiceFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    private void defaultAgencyServiceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgencyServiceShouldBeFound(shouldBeFound);
        defaultAgencyServiceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgencyServiceShouldBeFound(String filter) throws Exception {
        restAgencyServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyService.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));

        // Check, that the count call also returns 1
        restAgencyServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgencyServiceShouldNotBeFound(String filter) throws Exception {
        restAgencyServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgencyServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgencyService() throws Exception {
        // Get the agencyService
        restAgencyServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgencyService() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyService
        AgencyService updatedAgencyService = agencyServiceRepository.findById(agencyService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgencyService are not directly saved in db
        em.detach(updatedAgencyService);
        updatedAgencyService.title(UPDATED_TITLE).icon(UPDATED_ICON).content(UPDATED_CONTENT);
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(updatedAgencyService);

        restAgencyServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgencyServiceToMatchAllProperties(updatedAgencyService);
    }

    @Test
    @Transactional
    void putNonExistingAgencyService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyService.setId(longCount.incrementAndGet());

        // Create the AgencyService
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgencyService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyService.setId(longCount.incrementAndGet());

        // Create the AgencyService
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgencyService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyService.setId(longCount.incrementAndGet());

        // Create the AgencyService
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgencyServiceWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyService using partial update
        AgencyService partialUpdatedAgencyService = new AgencyService();
        partialUpdatedAgencyService.setId(agencyService.getId());

        partialUpdatedAgencyService.content(UPDATED_CONTENT);

        restAgencyServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyService))
            )
            .andExpect(status().isOk());

        // Validate the AgencyService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgencyService, agencyService),
            getPersistedAgencyService(agencyService)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgencyServiceWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyService using partial update
        AgencyService partialUpdatedAgencyService = new AgencyService();
        partialUpdatedAgencyService.setId(agencyService.getId());

        partialUpdatedAgencyService.title(UPDATED_TITLE).icon(UPDATED_ICON).content(UPDATED_CONTENT);

        restAgencyServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyService))
            )
            .andExpect(status().isOk());

        // Validate the AgencyService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyServiceUpdatableFieldsEquals(partialUpdatedAgencyService, getPersistedAgencyService(partialUpdatedAgencyService));
    }

    @Test
    @Transactional
    void patchNonExistingAgencyService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyService.setId(longCount.incrementAndGet());

        // Create the AgencyService
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agencyServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgencyService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyService.setId(longCount.incrementAndGet());

        // Create the AgencyService
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgencyService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyService.setId(longCount.incrementAndGet());

        // Create the AgencyService
        AgencyServiceDTO agencyServiceDTO = agencyServiceMapper.toDto(agencyService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agencyServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgencyService() throws Exception {
        // Initialize the database
        insertedAgencyService = agencyServiceRepository.saveAndFlush(agencyService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agencyService
        restAgencyServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, agencyService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agencyServiceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AgencyService getPersistedAgencyService(AgencyService agencyService) {
        return agencyServiceRepository.findById(agencyService.getId()).orElseThrow();
    }

    protected void assertPersistedAgencyServiceToMatchAllProperties(AgencyService expectedAgencyService) {
        assertAgencyServiceAllPropertiesEquals(expectedAgencyService, getPersistedAgencyService(expectedAgencyService));
    }

    protected void assertPersistedAgencyServiceToMatchUpdatableProperties(AgencyService expectedAgencyService) {
        assertAgencyServiceAllUpdatablePropertiesEquals(expectedAgencyService, getPersistedAgencyService(expectedAgencyService));
    }
}
