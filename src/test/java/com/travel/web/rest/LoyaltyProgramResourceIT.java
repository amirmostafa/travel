package com.travel.web.rest;

import static com.travel.domain.LoyaltyProgramAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.LoyaltyProgram;
import com.travel.repository.LoyaltyProgramRepository;
import com.travel.service.dto.LoyaltyProgramDTO;
import com.travel.service.mapper.LoyaltyProgramMapper;
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
 * Integration tests for the {@link LoyaltyProgramResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoyaltyProgramResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS_PER_DOLLAR = 0;
    private static final Integer UPDATED_POINTS_PER_DOLLAR = 1;
    private static final Integer SMALLER_POINTS_PER_DOLLAR = 0 - 1;

    private static final Integer DEFAULT_REWARD_THRESHOLD = 0;
    private static final Integer UPDATED_REWARD_THRESHOLD = 1;
    private static final Integer SMALLER_REWARD_THRESHOLD = 0 - 1;

    private static final String ENTITY_API_URL = "/api/loyalty-programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoyaltyProgramRepository loyaltyProgramRepository;

    @Autowired
    private LoyaltyProgramMapper loyaltyProgramMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoyaltyProgramMockMvc;

    private LoyaltyProgram loyaltyProgram;

    private LoyaltyProgram insertedLoyaltyProgram;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyProgram createEntity(EntityManager em) {
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .pointsPerDollar(DEFAULT_POINTS_PER_DOLLAR)
            .rewardThreshold(DEFAULT_REWARD_THRESHOLD);
        return loyaltyProgram;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyProgram createUpdatedEntity(EntityManager em) {
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pointsPerDollar(UPDATED_POINTS_PER_DOLLAR)
            .rewardThreshold(UPDATED_REWARD_THRESHOLD);
        return loyaltyProgram;
    }

    @BeforeEach
    public void initTest() {
        loyaltyProgram = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLoyaltyProgram != null) {
            loyaltyProgramRepository.delete(insertedLoyaltyProgram);
            insertedLoyaltyProgram = null;
        }
    }

    @Test
    @Transactional
    void createLoyaltyProgram() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);
        var returnedLoyaltyProgramDTO = om.readValue(
            restLoyaltyProgramMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyProgramDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LoyaltyProgramDTO.class
        );

        // Validate the LoyaltyProgram in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLoyaltyProgram = loyaltyProgramMapper.toEntity(returnedLoyaltyProgramDTO);
        assertLoyaltyProgramUpdatableFieldsEquals(returnedLoyaltyProgram, getPersistedLoyaltyProgram(returnedLoyaltyProgram));

        insertedLoyaltyProgram = returnedLoyaltyProgram;
    }

    @Test
    @Transactional
    void createLoyaltyProgramWithExistingId() throws Exception {
        // Create the LoyaltyProgram with an existing ID
        loyaltyProgram.setId(1L);
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoyaltyProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyProgramDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyProgram.setName(null);

        // Create the LoyaltyProgram, which fails.
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        restLoyaltyProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyProgramDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsPerDollarIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyProgram.setPointsPerDollar(null);

        // Create the LoyaltyProgram, which fails.
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        restLoyaltyProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyProgramDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRewardThresholdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyProgram.setRewardThreshold(null);

        // Create the LoyaltyProgram, which fails.
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        restLoyaltyProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyProgramDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoyaltyPrograms() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loyaltyProgram.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pointsPerDollar").value(hasItem(DEFAULT_POINTS_PER_DOLLAR)))
            .andExpect(jsonPath("$.[*].rewardThreshold").value(hasItem(DEFAULT_REWARD_THRESHOLD)));
    }

    @Test
    @Transactional
    void getLoyaltyProgram() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get the loyaltyProgram
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, loyaltyProgram.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loyaltyProgram.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.pointsPerDollar").value(DEFAULT_POINTS_PER_DOLLAR))
            .andExpect(jsonPath("$.rewardThreshold").value(DEFAULT_REWARD_THRESHOLD));
    }

    @Test
    @Transactional
    void getLoyaltyProgramsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        Long id = loyaltyProgram.getId();

        defaultLoyaltyProgramFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLoyaltyProgramFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLoyaltyProgramFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where name equals to
        defaultLoyaltyProgramFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where name in
        defaultLoyaltyProgramFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where name is not null
        defaultLoyaltyProgramFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where name contains
        defaultLoyaltyProgramFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where name does not contain
        defaultLoyaltyProgramFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where description equals to
        defaultLoyaltyProgramFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where description in
        defaultLoyaltyProgramFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where description is not null
        defaultLoyaltyProgramFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where description contains
        defaultLoyaltyProgramFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where description does not contain
        defaultLoyaltyProgramFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByPointsPerDollarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where pointsPerDollar equals to
        defaultLoyaltyProgramFiltering(
            "pointsPerDollar.equals=" + DEFAULT_POINTS_PER_DOLLAR,
            "pointsPerDollar.equals=" + UPDATED_POINTS_PER_DOLLAR
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByPointsPerDollarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where pointsPerDollar in
        defaultLoyaltyProgramFiltering(
            "pointsPerDollar.in=" + DEFAULT_POINTS_PER_DOLLAR + "," + UPDATED_POINTS_PER_DOLLAR,
            "pointsPerDollar.in=" + UPDATED_POINTS_PER_DOLLAR
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByPointsPerDollarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where pointsPerDollar is not null
        defaultLoyaltyProgramFiltering("pointsPerDollar.specified=true", "pointsPerDollar.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByPointsPerDollarIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where pointsPerDollar is greater than or equal to
        defaultLoyaltyProgramFiltering(
            "pointsPerDollar.greaterThanOrEqual=" + DEFAULT_POINTS_PER_DOLLAR,
            "pointsPerDollar.greaterThanOrEqual=" + UPDATED_POINTS_PER_DOLLAR
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByPointsPerDollarIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where pointsPerDollar is less than or equal to
        defaultLoyaltyProgramFiltering(
            "pointsPerDollar.lessThanOrEqual=" + DEFAULT_POINTS_PER_DOLLAR,
            "pointsPerDollar.lessThanOrEqual=" + SMALLER_POINTS_PER_DOLLAR
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByPointsPerDollarIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where pointsPerDollar is less than
        defaultLoyaltyProgramFiltering(
            "pointsPerDollar.lessThan=" + UPDATED_POINTS_PER_DOLLAR,
            "pointsPerDollar.lessThan=" + DEFAULT_POINTS_PER_DOLLAR
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByPointsPerDollarIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where pointsPerDollar is greater than
        defaultLoyaltyProgramFiltering(
            "pointsPerDollar.greaterThan=" + SMALLER_POINTS_PER_DOLLAR,
            "pointsPerDollar.greaterThan=" + DEFAULT_POINTS_PER_DOLLAR
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByRewardThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where rewardThreshold equals to
        defaultLoyaltyProgramFiltering(
            "rewardThreshold.equals=" + DEFAULT_REWARD_THRESHOLD,
            "rewardThreshold.equals=" + UPDATED_REWARD_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByRewardThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where rewardThreshold in
        defaultLoyaltyProgramFiltering(
            "rewardThreshold.in=" + DEFAULT_REWARD_THRESHOLD + "," + UPDATED_REWARD_THRESHOLD,
            "rewardThreshold.in=" + UPDATED_REWARD_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByRewardThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where rewardThreshold is not null
        defaultLoyaltyProgramFiltering("rewardThreshold.specified=true", "rewardThreshold.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByRewardThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where rewardThreshold is greater than or equal to
        defaultLoyaltyProgramFiltering(
            "rewardThreshold.greaterThanOrEqual=" + DEFAULT_REWARD_THRESHOLD,
            "rewardThreshold.greaterThanOrEqual=" + UPDATED_REWARD_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByRewardThresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where rewardThreshold is less than or equal to
        defaultLoyaltyProgramFiltering(
            "rewardThreshold.lessThanOrEqual=" + DEFAULT_REWARD_THRESHOLD,
            "rewardThreshold.lessThanOrEqual=" + SMALLER_REWARD_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByRewardThresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where rewardThreshold is less than
        defaultLoyaltyProgramFiltering(
            "rewardThreshold.lessThan=" + UPDATED_REWARD_THRESHOLD,
            "rewardThreshold.lessThan=" + DEFAULT_REWARD_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyProgramsByRewardThresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList where rewardThreshold is greater than
        defaultLoyaltyProgramFiltering(
            "rewardThreshold.greaterThan=" + SMALLER_REWARD_THRESHOLD,
            "rewardThreshold.greaterThan=" + DEFAULT_REWARD_THRESHOLD
        );
    }

    private void defaultLoyaltyProgramFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLoyaltyProgramShouldBeFound(shouldBeFound);
        defaultLoyaltyProgramShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoyaltyProgramShouldBeFound(String filter) throws Exception {
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loyaltyProgram.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pointsPerDollar").value(hasItem(DEFAULT_POINTS_PER_DOLLAR)))
            .andExpect(jsonPath("$.[*].rewardThreshold").value(hasItem(DEFAULT_REWARD_THRESHOLD)));

        // Check, that the count call also returns 1
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoyaltyProgramShouldNotBeFound(String filter) throws Exception {
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoyaltyProgram() throws Exception {
        // Get the loyaltyProgram
        restLoyaltyProgramMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoyaltyProgram() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyProgram
        LoyaltyProgram updatedLoyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgram.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLoyaltyProgram are not directly saved in db
        em.detach(updatedLoyaltyProgram);
        updatedLoyaltyProgram
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pointsPerDollar(UPDATED_POINTS_PER_DOLLAR)
            .rewardThreshold(UPDATED_REWARD_THRESHOLD);
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(updatedLoyaltyProgram);

        restLoyaltyProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyProgramDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoyaltyProgramToMatchAllProperties(updatedLoyaltyProgram);
    }

    @Test
    @Transactional
    void putNonExistingLoyaltyProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyProgram.setId(longCount.incrementAndGet());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyProgramDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoyaltyProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyProgram.setId(longCount.incrementAndGet());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoyaltyProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyProgram.setId(longCount.incrementAndGet());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyProgramDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoyaltyProgramWithPatch() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyProgram using partial update
        LoyaltyProgram partialUpdatedLoyaltyProgram = new LoyaltyProgram();
        partialUpdatedLoyaltyProgram.setId(loyaltyProgram.getId());

        partialUpdatedLoyaltyProgram.description(UPDATED_DESCRIPTION).pointsPerDollar(UPDATED_POINTS_PER_DOLLAR);

        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoyaltyProgram))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyProgram in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoyaltyProgramUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLoyaltyProgram, loyaltyProgram),
            getPersistedLoyaltyProgram(loyaltyProgram)
        );
    }

    @Test
    @Transactional
    void fullUpdateLoyaltyProgramWithPatch() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyProgram using partial update
        LoyaltyProgram partialUpdatedLoyaltyProgram = new LoyaltyProgram();
        partialUpdatedLoyaltyProgram.setId(loyaltyProgram.getId());

        partialUpdatedLoyaltyProgram
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pointsPerDollar(UPDATED_POINTS_PER_DOLLAR)
            .rewardThreshold(UPDATED_REWARD_THRESHOLD);

        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoyaltyProgram))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyProgram in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoyaltyProgramUpdatableFieldsEquals(partialUpdatedLoyaltyProgram, getPersistedLoyaltyProgram(partialUpdatedLoyaltyProgram));
    }

    @Test
    @Transactional
    void patchNonExistingLoyaltyProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyProgram.setId(longCount.incrementAndGet());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loyaltyProgramDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoyaltyProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyProgram.setId(longCount.incrementAndGet());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoyaltyProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyProgram.setId(longCount.incrementAndGet());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loyaltyProgramDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyProgram in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoyaltyProgram() throws Exception {
        // Initialize the database
        insertedLoyaltyProgram = loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the loyaltyProgram
        restLoyaltyProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, loyaltyProgram.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return loyaltyProgramRepository.count();
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

    protected LoyaltyProgram getPersistedLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        return loyaltyProgramRepository.findById(loyaltyProgram.getId()).orElseThrow();
    }

    protected void assertPersistedLoyaltyProgramToMatchAllProperties(LoyaltyProgram expectedLoyaltyProgram) {
        assertLoyaltyProgramAllPropertiesEquals(expectedLoyaltyProgram, getPersistedLoyaltyProgram(expectedLoyaltyProgram));
    }

    protected void assertPersistedLoyaltyProgramToMatchUpdatableProperties(LoyaltyProgram expectedLoyaltyProgram) {
        assertLoyaltyProgramAllUpdatablePropertiesEquals(expectedLoyaltyProgram, getPersistedLoyaltyProgram(expectedLoyaltyProgram));
    }
}
