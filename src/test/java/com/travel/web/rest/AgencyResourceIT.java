package com.travel.web.rest;

import static com.travel.domain.AgencyAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.Agency;
import com.travel.repository.AgencyRepository;
import com.travel.service.dto.AgencyDTO;
import com.travel.service.mapper.AgencyMapper;
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
 * Integration tests for the {@link AgencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "Z@f13m.yXa7u";
    private static final String UPDATED_EMAIL = "{@mjp.<e";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private AgencyMapper agencyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyMockMvc;

    private Agency agency;

    private Agency insertedAgency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createEntity(EntityManager em) {
        Agency agency = new Agency()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE);
        return agency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createUpdatedEntity(EntityManager em) {
        Agency agency = new Agency()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);
        return agency;
    }

    @BeforeEach
    public void initTest() {
        agency = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgency != null) {
            agencyRepository.delete(insertedAgency);
            insertedAgency = null;
        }
    }

    @Test
    @Transactional
    void createAgency() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);
        var returnedAgencyDTO = om.readValue(
            restAgencyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgencyDTO.class
        );

        // Validate the Agency in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAgency = agencyMapper.toEntity(returnedAgencyDTO);
        assertAgencyUpdatableFieldsEquals(returnedAgency, getPersistedAgency(returnedAgency));

        insertedAgency = returnedAgency;
    }

    @Test
    @Transactional
    void createAgencyWithExistingId() throws Exception {
        // Create the Agency with an existing ID
        agency.setId(1L);
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agency.setName(null);

        // Create the Agency, which fails.
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agency.setAddress(null);

        // Create the Agency, which fails.
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agency.setContactNumber(null);

        // Create the Agency, which fails.
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agency.setEmail(null);

        // Create the Agency, which fails.
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgencies() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }

    @Test
    @Transactional
    void getAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get the agency
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL_ID, agency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE));
    }

    @Test
    @Transactional
    void getAgenciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        Long id = agency.getId();

        defaultAgencyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAgencyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAgencyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name equals to
        defaultAgencyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name in
        defaultAgencyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name is not null
        defaultAgencyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name contains
        defaultAgencyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name does not contain
        defaultAgencyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address equals to
        defaultAgencyFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address in
        defaultAgencyFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address is not null
        defaultAgencyFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address contains
        defaultAgencyFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address does not contain
        defaultAgencyFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where contactNumber equals to
        defaultAgencyFiltering("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER, "contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgenciesByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where contactNumber in
        defaultAgencyFiltering(
            "contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER,
            "contactNumber.in=" + UPDATED_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAgenciesByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where contactNumber is not null
        defaultAgencyFiltering("contactNumber.specified=true", "contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where contactNumber contains
        defaultAgencyFiltering("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER, "contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgenciesByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where contactNumber does not contain
        defaultAgencyFiltering(
            "contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER,
            "contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAgenciesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where email equals to
        defaultAgencyFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAgenciesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where email in
        defaultAgencyFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAgenciesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where email is not null
        defaultAgencyFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where email contains
        defaultAgencyFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAgenciesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where email does not contain
        defaultAgencyFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllAgenciesByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where website equals to
        defaultAgencyFiltering("website.equals=" + DEFAULT_WEBSITE, "website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllAgenciesByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where website in
        defaultAgencyFiltering("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE, "website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllAgenciesByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where website is not null
        defaultAgencyFiltering("website.specified=true", "website.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where website contains
        defaultAgencyFiltering("website.contains=" + DEFAULT_WEBSITE, "website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllAgenciesByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where website does not contain
        defaultAgencyFiltering("website.doesNotContain=" + UPDATED_WEBSITE, "website.doesNotContain=" + DEFAULT_WEBSITE);
    }

    private void defaultAgencyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgencyShouldBeFound(shouldBeFound);
        defaultAgencyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgencyShouldBeFound(String filter) throws Exception {
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));

        // Check, that the count call also returns 1
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgencyShouldNotBeFound(String filter) throws Exception {
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgency() throws Exception {
        // Get the agency
        restAgencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency
        Agency updatedAgency = agencyRepository.findById(agency.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgency are not directly saved in db
        em.detach(updatedAgency);
        updatedAgency
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);
        AgencyDTO agencyDTO = agencyMapper.toDto(updatedAgency);

        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgencyToMatchAllProperties(updatedAgency);
    }

    @Test
    @Transactional
    void putNonExistingAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency.name(UPDATED_NAME);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAgency, agency), getPersistedAgency(agency));
    }

    @Test
    @Transactional
    void fullUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyUpdatableFieldsEquals(partialUpdatedAgency, getPersistedAgency(partialUpdatedAgency));
    }

    @Test
    @Transactional
    void patchNonExistingAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agency
        restAgencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, agency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agencyRepository.count();
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

    protected Agency getPersistedAgency(Agency agency) {
        return agencyRepository.findById(agency.getId()).orElseThrow();
    }

    protected void assertPersistedAgencyToMatchAllProperties(Agency expectedAgency) {
        assertAgencyAllPropertiesEquals(expectedAgency, getPersistedAgency(expectedAgency));
    }

    protected void assertPersistedAgencyToMatchUpdatableProperties(Agency expectedAgency) {
        assertAgencyAllUpdatablePropertiesEquals(expectedAgency, getPersistedAgency(expectedAgency));
    }
}
