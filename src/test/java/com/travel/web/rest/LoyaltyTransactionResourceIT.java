package com.travel.web.rest;

import static com.travel.domain.LoyaltyTransactionAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.Customer;
import com.travel.domain.LoyaltyTransaction;
import com.travel.domain.enumeration.TransactionType;
import com.travel.repository.LoyaltyTransactionRepository;
import com.travel.service.dto.LoyaltyTransactionDTO;
import com.travel.service.mapper.LoyaltyTransactionMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link LoyaltyTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoyaltyTransactionResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.EARNED;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.REDEEMED;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/loyalty-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    @Autowired
    private LoyaltyTransactionMapper loyaltyTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoyaltyTransactionMockMvc;

    private LoyaltyTransaction loyaltyTransaction;

    private LoyaltyTransaction insertedLoyaltyTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyTransaction createEntity(EntityManager em) {
        LoyaltyTransaction loyaltyTransaction = new LoyaltyTransaction()
            .date(DEFAULT_DATE)
            .points(DEFAULT_POINTS)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return loyaltyTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyTransaction createUpdatedEntity(EntityManager em) {
        LoyaltyTransaction loyaltyTransaction = new LoyaltyTransaction()
            .date(UPDATED_DATE)
            .points(UPDATED_POINTS)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .description(UPDATED_DESCRIPTION);
        return loyaltyTransaction;
    }

    @BeforeEach
    public void initTest() {
        loyaltyTransaction = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLoyaltyTransaction != null) {
            loyaltyTransactionRepository.delete(insertedLoyaltyTransaction);
            insertedLoyaltyTransaction = null;
        }
    }

    @Test
    @Transactional
    void createLoyaltyTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LoyaltyTransaction
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);
        var returnedLoyaltyTransactionDTO = om.readValue(
            restLoyaltyTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyTransactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LoyaltyTransactionDTO.class
        );

        // Validate the LoyaltyTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLoyaltyTransaction = loyaltyTransactionMapper.toEntity(returnedLoyaltyTransactionDTO);
        assertLoyaltyTransactionUpdatableFieldsEquals(
            returnedLoyaltyTransaction,
            getPersistedLoyaltyTransaction(returnedLoyaltyTransaction)
        );

        insertedLoyaltyTransaction = returnedLoyaltyTransaction;
    }

    @Test
    @Transactional
    void createLoyaltyTransactionWithExistingId() throws Exception {
        // Create the LoyaltyTransaction with an existing ID
        loyaltyTransaction.setId(1L);
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoyaltyTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyTransaction.setDate(null);

        // Create the LoyaltyTransaction, which fails.
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        restLoyaltyTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyTransaction.setPoints(null);

        // Create the LoyaltyTransaction, which fails.
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        restLoyaltyTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyTransaction.setTransactionType(null);

        // Create the LoyaltyTransaction, which fails.
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        restLoyaltyTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactions() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList
        restLoyaltyTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loyaltyTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getLoyaltyTransaction() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get the loyaltyTransaction
        restLoyaltyTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, loyaltyTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loyaltyTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getLoyaltyTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        Long id = loyaltyTransaction.getId();

        defaultLoyaltyTransactionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLoyaltyTransactionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLoyaltyTransactionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where date equals to
        defaultLoyaltyTransactionFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where date in
        defaultLoyaltyTransactionFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where date is not null
        defaultLoyaltyTransactionFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where date is greater than or equal to
        defaultLoyaltyTransactionFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where date is less than or equal to
        defaultLoyaltyTransactionFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where date is less than
        defaultLoyaltyTransactionFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where date is greater than
        defaultLoyaltyTransactionFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where points equals to
        defaultLoyaltyTransactionFiltering("points.equals=" + DEFAULT_POINTS, "points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where points in
        defaultLoyaltyTransactionFiltering("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS, "points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where points is not null
        defaultLoyaltyTransactionFiltering("points.specified=true", "points.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where points is greater than or equal to
        defaultLoyaltyTransactionFiltering("points.greaterThanOrEqual=" + DEFAULT_POINTS, "points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where points is less than or equal to
        defaultLoyaltyTransactionFiltering("points.lessThanOrEqual=" + DEFAULT_POINTS, "points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where points is less than
        defaultLoyaltyTransactionFiltering("points.lessThan=" + UPDATED_POINTS, "points.lessThan=" + DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where points is greater than
        defaultLoyaltyTransactionFiltering("points.greaterThan=" + SMALLER_POINTS, "points.greaterThan=" + DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where transactionType equals to
        defaultLoyaltyTransactionFiltering(
            "transactionType.equals=" + DEFAULT_TRANSACTION_TYPE,
            "transactionType.equals=" + UPDATED_TRANSACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where transactionType in
        defaultLoyaltyTransactionFiltering(
            "transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE,
            "transactionType.in=" + UPDATED_TRANSACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where transactionType is not null
        defaultLoyaltyTransactionFiltering("transactionType.specified=true", "transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where description equals to
        defaultLoyaltyTransactionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where description in
        defaultLoyaltyTransactionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where description is not null
        defaultLoyaltyTransactionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where description contains
        defaultLoyaltyTransactionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        // Get all the loyaltyTransactionList where description does not contain
        defaultLoyaltyTransactionFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLoyaltyTransactionsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        loyaltyTransaction.setCustomer(customer);
        loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);
        Long customerId = customer.getId();
        // Get all the loyaltyTransactionList where customer equals to customerId
        defaultLoyaltyTransactionShouldBeFound("customerId.equals=" + customerId);

        // Get all the loyaltyTransactionList where customer equals to (customerId + 1)
        defaultLoyaltyTransactionShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultLoyaltyTransactionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLoyaltyTransactionShouldBeFound(shouldBeFound);
        defaultLoyaltyTransactionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoyaltyTransactionShouldBeFound(String filter) throws Exception {
        restLoyaltyTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loyaltyTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restLoyaltyTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoyaltyTransactionShouldNotBeFound(String filter) throws Exception {
        restLoyaltyTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoyaltyTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoyaltyTransaction() throws Exception {
        // Get the loyaltyTransaction
        restLoyaltyTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoyaltyTransaction() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyTransaction
        LoyaltyTransaction updatedLoyaltyTransaction = loyaltyTransactionRepository.findById(loyaltyTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLoyaltyTransaction are not directly saved in db
        em.detach(updatedLoyaltyTransaction);
        updatedLoyaltyTransaction
            .date(UPDATED_DATE)
            .points(UPDATED_POINTS)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .description(UPDATED_DESCRIPTION);
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(updatedLoyaltyTransaction);

        restLoyaltyTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoyaltyTransactionToMatchAllProperties(updatedLoyaltyTransaction);
    }

    @Test
    @Transactional
    void putNonExistingLoyaltyTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyTransaction.setId(longCount.incrementAndGet());

        // Create the LoyaltyTransaction
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoyaltyTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyTransaction.setId(longCount.incrementAndGet());

        // Create the LoyaltyTransaction
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoyaltyTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyTransaction.setId(longCount.incrementAndGet());

        // Create the LoyaltyTransaction
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoyaltyTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyTransaction using partial update
        LoyaltyTransaction partialUpdatedLoyaltyTransaction = new LoyaltyTransaction();
        partialUpdatedLoyaltyTransaction.setId(loyaltyTransaction.getId());

        partialUpdatedLoyaltyTransaction.date(UPDATED_DATE).transactionType(UPDATED_TRANSACTION_TYPE).description(UPDATED_DESCRIPTION);

        restLoyaltyTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoyaltyTransaction))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoyaltyTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLoyaltyTransaction, loyaltyTransaction),
            getPersistedLoyaltyTransaction(loyaltyTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateLoyaltyTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyTransaction using partial update
        LoyaltyTransaction partialUpdatedLoyaltyTransaction = new LoyaltyTransaction();
        partialUpdatedLoyaltyTransaction.setId(loyaltyTransaction.getId());

        partialUpdatedLoyaltyTransaction
            .date(UPDATED_DATE)
            .points(UPDATED_POINTS)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .description(UPDATED_DESCRIPTION);

        restLoyaltyTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoyaltyTransaction))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoyaltyTransactionUpdatableFieldsEquals(
            partialUpdatedLoyaltyTransaction,
            getPersistedLoyaltyTransaction(partialUpdatedLoyaltyTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLoyaltyTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyTransaction.setId(longCount.incrementAndGet());

        // Create the LoyaltyTransaction
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loyaltyTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loyaltyTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoyaltyTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyTransaction.setId(longCount.incrementAndGet());

        // Create the LoyaltyTransaction
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loyaltyTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoyaltyTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyTransaction.setId(longCount.incrementAndGet());

        // Create the LoyaltyTransaction
        LoyaltyTransactionDTO loyaltyTransactionDTO = loyaltyTransactionMapper.toDto(loyaltyTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loyaltyTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoyaltyTransaction() throws Exception {
        // Initialize the database
        insertedLoyaltyTransaction = loyaltyTransactionRepository.saveAndFlush(loyaltyTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the loyaltyTransaction
        restLoyaltyTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, loyaltyTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return loyaltyTransactionRepository.count();
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

    protected LoyaltyTransaction getPersistedLoyaltyTransaction(LoyaltyTransaction loyaltyTransaction) {
        return loyaltyTransactionRepository.findById(loyaltyTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedLoyaltyTransactionToMatchAllProperties(LoyaltyTransaction expectedLoyaltyTransaction) {
        assertLoyaltyTransactionAllPropertiesEquals(expectedLoyaltyTransaction, getPersistedLoyaltyTransaction(expectedLoyaltyTransaction));
    }

    protected void assertPersistedLoyaltyTransactionToMatchUpdatableProperties(LoyaltyTransaction expectedLoyaltyTransaction) {
        assertLoyaltyTransactionAllUpdatablePropertiesEquals(
            expectedLoyaltyTransaction,
            getPersistedLoyaltyTransaction(expectedLoyaltyTransaction)
        );
    }
}
