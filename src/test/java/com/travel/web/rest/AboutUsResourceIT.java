package com.travel.web.rest;

import static com.travel.domain.AboutUsAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.AboutUs;
import com.travel.repository.AboutUsRepository;
import com.travel.service.dto.AboutUsDTO;
import com.travel.service.mapper.AboutUsMapper;
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
 * Integration tests for the {@link AboutUsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AboutUsResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aboutuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AboutUsRepository aboutUsRepository;

    @Autowired
    private AboutUsMapper aboutUsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAboutUsMockMvc;

    private AboutUs aboutUs;

    private AboutUs insertedAboutUs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutUs createEntity(EntityManager em) {
        AboutUs aboutUs = new AboutUs()
            .description(DEFAULT_DESCRIPTION)
            .contactDetails(DEFAULT_CONTACT_DETAILS)
            .additionalInfo(DEFAULT_ADDITIONAL_INFO);
        return aboutUs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutUs createUpdatedEntity(EntityManager em) {
        AboutUs aboutUs = new AboutUs()
            .description(UPDATED_DESCRIPTION)
            .contactDetails(UPDATED_CONTACT_DETAILS)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);
        return aboutUs;
    }

    @BeforeEach
    public void initTest() {
        aboutUs = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAboutUs != null) {
            aboutUsRepository.delete(insertedAboutUs);
            insertedAboutUs = null;
        }
    }

    @Test
    @Transactional
    void createAboutUs() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AboutUs
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);
        var returnedAboutUsDTO = om.readValue(
            restAboutUsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aboutUsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AboutUsDTO.class
        );

        // Validate the AboutUs in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAboutUs = aboutUsMapper.toEntity(returnedAboutUsDTO);
        assertAboutUsUpdatableFieldsEquals(returnedAboutUs, getPersistedAboutUs(returnedAboutUs));

        insertedAboutUs = returnedAboutUs;
    }

    @Test
    @Transactional
    void createAboutUsWithExistingId() throws Exception {
        // Create the AboutUs with an existing ID
        aboutUs.setId(1L);
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAboutUsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aboutUsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aboutUs.setDescription(null);

        // Create the AboutUs, which fails.
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        restAboutUsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aboutUsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAboutuses() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aboutUs.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contactDetails").value(hasItem(DEFAULT_CONTACT_DETAILS)))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO)));
    }

    @Test
    @Transactional
    void getAboutUs() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get the aboutUs
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL_ID, aboutUs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aboutUs.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.contactDetails").value(DEFAULT_CONTACT_DETAILS))
            .andExpect(jsonPath("$.additionalInfo").value(DEFAULT_ADDITIONAL_INFO));
    }

    @Test
    @Transactional
    void getAboutusesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        Long id = aboutUs.getId();

        defaultAboutUsFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAboutUsFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAboutUsFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAboutusesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where description equals to
        defaultAboutUsFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAboutusesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where description in
        defaultAboutUsFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAboutusesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where description is not null
        defaultAboutUsFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAboutusesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where description contains
        defaultAboutUsFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAboutusesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where description does not contain
        defaultAboutUsFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAboutusesByContactDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where contactDetails equals to
        defaultAboutUsFiltering("contactDetails.equals=" + DEFAULT_CONTACT_DETAILS, "contactDetails.equals=" + UPDATED_CONTACT_DETAILS);
    }

    @Test
    @Transactional
    void getAllAboutusesByContactDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where contactDetails in
        defaultAboutUsFiltering(
            "contactDetails.in=" + DEFAULT_CONTACT_DETAILS + "," + UPDATED_CONTACT_DETAILS,
            "contactDetails.in=" + UPDATED_CONTACT_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllAboutusesByContactDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where contactDetails is not null
        defaultAboutUsFiltering("contactDetails.specified=true", "contactDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllAboutusesByContactDetailsContainsSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where contactDetails contains
        defaultAboutUsFiltering("contactDetails.contains=" + DEFAULT_CONTACT_DETAILS, "contactDetails.contains=" + UPDATED_CONTACT_DETAILS);
    }

    @Test
    @Transactional
    void getAllAboutusesByContactDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where contactDetails does not contain
        defaultAboutUsFiltering(
            "contactDetails.doesNotContain=" + UPDATED_CONTACT_DETAILS,
            "contactDetails.doesNotContain=" + DEFAULT_CONTACT_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllAboutusesByAdditionalInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where additionalInfo equals to
        defaultAboutUsFiltering("additionalInfo.equals=" + DEFAULT_ADDITIONAL_INFO, "additionalInfo.equals=" + UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    void getAllAboutusesByAdditionalInfoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where additionalInfo in
        defaultAboutUsFiltering(
            "additionalInfo.in=" + DEFAULT_ADDITIONAL_INFO + "," + UPDATED_ADDITIONAL_INFO,
            "additionalInfo.in=" + UPDATED_ADDITIONAL_INFO
        );
    }

    @Test
    @Transactional
    void getAllAboutusesByAdditionalInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where additionalInfo is not null
        defaultAboutUsFiltering("additionalInfo.specified=true", "additionalInfo.specified=false");
    }

    @Test
    @Transactional
    void getAllAboutusesByAdditionalInfoContainsSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where additionalInfo contains
        defaultAboutUsFiltering("additionalInfo.contains=" + DEFAULT_ADDITIONAL_INFO, "additionalInfo.contains=" + UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    void getAllAboutusesByAdditionalInfoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList where additionalInfo does not contain
        defaultAboutUsFiltering(
            "additionalInfo.doesNotContain=" + UPDATED_ADDITIONAL_INFO,
            "additionalInfo.doesNotContain=" + DEFAULT_ADDITIONAL_INFO
        );
    }

    private void defaultAboutUsFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAboutUsShouldBeFound(shouldBeFound);
        defaultAboutUsShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAboutUsShouldBeFound(String filter) throws Exception {
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aboutUs.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contactDetails").value(hasItem(DEFAULT_CONTACT_DETAILS)))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO)));

        // Check, that the count call also returns 1
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAboutUsShouldNotBeFound(String filter) throws Exception {
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAboutUs() throws Exception {
        // Get the aboutUs
        restAboutUsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAboutUs() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aboutUs
        AboutUs updatedAboutUs = aboutUsRepository.findById(aboutUs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAboutUs are not directly saved in db
        em.detach(updatedAboutUs);
        updatedAboutUs.description(UPDATED_DESCRIPTION).contactDetails(UPDATED_CONTACT_DETAILS).additionalInfo(UPDATED_ADDITIONAL_INFO);
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(updatedAboutUs);

        restAboutUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aboutUsDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aboutUsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAboutUsToMatchAllProperties(updatedAboutUs);
    }

    @Test
    @Transactional
    void putNonExistingAboutUs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutUs.setId(longCount.incrementAndGet());

        // Create the AboutUs
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aboutUsDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aboutUsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAboutUs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutUs.setId(longCount.incrementAndGet());

        // Create the AboutUs
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aboutUsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAboutUs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutUs.setId(longCount.incrementAndGet());

        // Create the AboutUs
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aboutUsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAboutUsWithPatch() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aboutUs using partial update
        AboutUs partialUpdatedAboutUs = new AboutUs();
        partialUpdatedAboutUs.setId(aboutUs.getId());

        partialUpdatedAboutUs.contactDetails(UPDATED_CONTACT_DETAILS).additionalInfo(UPDATED_ADDITIONAL_INFO);

        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAboutUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAboutUs))
            )
            .andExpect(status().isOk());

        // Validate the AboutUs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAboutUsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAboutUs, aboutUs), getPersistedAboutUs(aboutUs));
    }

    @Test
    @Transactional
    void fullUpdateAboutUsWithPatch() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aboutUs using partial update
        AboutUs partialUpdatedAboutUs = new AboutUs();
        partialUpdatedAboutUs.setId(aboutUs.getId());

        partialUpdatedAboutUs
            .description(UPDATED_DESCRIPTION)
            .contactDetails(UPDATED_CONTACT_DETAILS)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);

        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAboutUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAboutUs))
            )
            .andExpect(status().isOk());

        // Validate the AboutUs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAboutUsUpdatableFieldsEquals(partialUpdatedAboutUs, getPersistedAboutUs(partialUpdatedAboutUs));
    }

    @Test
    @Transactional
    void patchNonExistingAboutUs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutUs.setId(longCount.incrementAndGet());

        // Create the AboutUs
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aboutUsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aboutUsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAboutUs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutUs.setId(longCount.incrementAndGet());

        // Create the AboutUs
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aboutUsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAboutUs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutUs.setId(longCount.incrementAndGet());

        // Create the AboutUs
        AboutUsDTO aboutUsDTO = aboutUsMapper.toDto(aboutUs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aboutUsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AboutUs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAboutUs() throws Exception {
        // Initialize the database
        insertedAboutUs = aboutUsRepository.saveAndFlush(aboutUs);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aboutUs
        restAboutUsMockMvc
            .perform(delete(ENTITY_API_URL_ID, aboutUs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aboutUsRepository.count();
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

    protected AboutUs getPersistedAboutUs(AboutUs aboutUs) {
        return aboutUsRepository.findById(aboutUs.getId()).orElseThrow();
    }

    protected void assertPersistedAboutUsToMatchAllProperties(AboutUs expectedAboutUs) {
        assertAboutUsAllPropertiesEquals(expectedAboutUs, getPersistedAboutUs(expectedAboutUs));
    }

    protected void assertPersistedAboutUsToMatchUpdatableProperties(AboutUs expectedAboutUs) {
        assertAboutUsAllUpdatablePropertiesEquals(expectedAboutUs, getPersistedAboutUs(expectedAboutUs));
    }
}
