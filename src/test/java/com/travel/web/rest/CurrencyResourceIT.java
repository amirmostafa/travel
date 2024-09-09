package com.travel.web.rest;

import static com.travel.domain.CurrencyAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.travel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.Currency;
import com.travel.repository.CurrencyRepository;
import com.travel.service.dto.CurrencyDTO;
import com.travel.service.mapper.CurrencyMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CurrencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CurrencyResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXCHANGE_RATE = new BigDecimal(0);
    private static final BigDecimal UPDATED_EXCHANGE_RATE = new BigDecimal(1);
    private static final BigDecimal SMALLER_EXCHANGE_RATE = new BigDecimal(0 - 1);

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    private Currency insertedCurrency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createEntity(EntityManager em) {
        Currency currency = new Currency()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .symbol(DEFAULT_SYMBOL)
            .exchangeRate(DEFAULT_EXCHANGE_RATE)
            .isDefault(DEFAULT_IS_DEFAULT);
        return currency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createUpdatedEntity(EntityManager em) {
        Currency currency = new Currency()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .exchangeRate(UPDATED_EXCHANGE_RATE)
            .isDefault(UPDATED_IS_DEFAULT);
        return currency;
    }

    @BeforeEach
    public void initTest() {
        currency = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCurrency != null) {
            currencyRepository.delete(insertedCurrency);
            insertedCurrency = null;
        }
    }

    @Test
    @Transactional
    void createCurrency() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);
        var returnedCurrencyDTO = om.readValue(
            restCurrencyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CurrencyDTO.class
        );

        // Validate the Currency in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCurrency = currencyMapper.toEntity(returnedCurrencyDTO);
        assertCurrencyUpdatableFieldsEquals(returnedCurrency, getPersistedCurrency(returnedCurrency));

        insertedCurrency = returnedCurrency;
    }

    @Test
    @Transactional
    void createCurrencyWithExistingId() throws Exception {
        // Create the Currency with an existing ID
        currency.setId(1L);
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        currency.setCode(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        currency.setName(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExchangeRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        currency.setExchangeRate(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDefaultIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        currency.setIsDefault(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCurrencies() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].exchangeRate").value(hasItem(sameNumber(DEFAULT_EXCHANGE_RATE))))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    void getCurrency() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.exchangeRate").value(sameNumber(DEFAULT_EXCHANGE_RATE)))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    void getCurrenciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        Long id = currency.getId();

        defaultCurrencyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCurrencyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCurrencyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code equals to
        defaultCurrencyFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code in
        defaultCurrencyFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code is not null
        defaultCurrencyFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code contains
        defaultCurrencyFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code does not contain
        defaultCurrencyFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name equals to
        defaultCurrencyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name in
        defaultCurrencyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name is not null
        defaultCurrencyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name contains
        defaultCurrencyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name does not contain
        defaultCurrencyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol equals to
        defaultCurrencyFiltering("symbol.equals=" + DEFAULT_SYMBOL, "symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol in
        defaultCurrencyFiltering("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL, "symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol is not null
        defaultCurrencyFiltering("symbol.specified=true", "symbol.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol contains
        defaultCurrencyFiltering("symbol.contains=" + DEFAULT_SYMBOL, "symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol does not contain
        defaultCurrencyFiltering("symbol.doesNotContain=" + UPDATED_SYMBOL, "symbol.doesNotContain=" + DEFAULT_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByExchangeRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where exchangeRate equals to
        defaultCurrencyFiltering("exchangeRate.equals=" + DEFAULT_EXCHANGE_RATE, "exchangeRate.equals=" + UPDATED_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByExchangeRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where exchangeRate in
        defaultCurrencyFiltering(
            "exchangeRate.in=" + DEFAULT_EXCHANGE_RATE + "," + UPDATED_EXCHANGE_RATE,
            "exchangeRate.in=" + UPDATED_EXCHANGE_RATE
        );
    }

    @Test
    @Transactional
    void getAllCurrenciesByExchangeRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where exchangeRate is not null
        defaultCurrencyFiltering("exchangeRate.specified=true", "exchangeRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByExchangeRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where exchangeRate is greater than or equal to
        defaultCurrencyFiltering(
            "exchangeRate.greaterThanOrEqual=" + DEFAULT_EXCHANGE_RATE,
            "exchangeRate.greaterThanOrEqual=" + UPDATED_EXCHANGE_RATE
        );
    }

    @Test
    @Transactional
    void getAllCurrenciesByExchangeRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where exchangeRate is less than or equal to
        defaultCurrencyFiltering(
            "exchangeRate.lessThanOrEqual=" + DEFAULT_EXCHANGE_RATE,
            "exchangeRate.lessThanOrEqual=" + SMALLER_EXCHANGE_RATE
        );
    }

    @Test
    @Transactional
    void getAllCurrenciesByExchangeRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where exchangeRate is less than
        defaultCurrencyFiltering("exchangeRate.lessThan=" + UPDATED_EXCHANGE_RATE, "exchangeRate.lessThan=" + DEFAULT_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByExchangeRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where exchangeRate is greater than
        defaultCurrencyFiltering("exchangeRate.greaterThan=" + SMALLER_EXCHANGE_RATE, "exchangeRate.greaterThan=" + DEFAULT_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefault equals to
        defaultCurrencyFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefault in
        defaultCurrencyFiltering("isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT, "isDefault.in=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefault is not null
        defaultCurrencyFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    private void defaultCurrencyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCurrencyShouldBeFound(shouldBeFound);
        defaultCurrencyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyShouldBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].exchangeRate").value(hasItem(sameNumber(DEFAULT_EXCHANGE_RATE))))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));

        // Check, that the count call also returns 1
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyShouldNotBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCurrency() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the currency
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency);
        updatedCurrency
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .exchangeRate(UPDATED_EXCHANGE_RATE)
            .isDefault(UPDATED_IS_DEFAULT);
        CurrencyDTO currencyDTO = currencyMapper.toDto(updatedCurrency);

        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(currencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCurrencyToMatchAllProperties(updatedCurrency);
    }

    @Test
    @Transactional
    void putNonExistingCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.symbol(UPDATED_SYMBOL).exchangeRate(UPDATED_EXCHANGE_RATE).isDefault(UPDATED_IS_DEFAULT);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCurrencyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCurrency, currency), getPersistedCurrency(currency));
    }

    @Test
    @Transactional
    void fullUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .exchangeRate(UPDATED_EXCHANGE_RATE)
            .isDefault(UPDATED_IS_DEFAULT);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCurrencyUpdatableFieldsEquals(partialUpdatedCurrency, getPersistedCurrency(partialUpdatedCurrency));
    }

    @Test
    @Transactional
    void patchNonExistingCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(currencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurrency() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the currency
        restCurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, currency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return currencyRepository.count();
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

    protected Currency getPersistedCurrency(Currency currency) {
        return currencyRepository.findById(currency.getId()).orElseThrow();
    }

    protected void assertPersistedCurrencyToMatchAllProperties(Currency expectedCurrency) {
        assertCurrencyAllPropertiesEquals(expectedCurrency, getPersistedCurrency(expectedCurrency));
    }

    protected void assertPersistedCurrencyToMatchUpdatableProperties(Currency expectedCurrency) {
        assertCurrencyAllUpdatablePropertiesEquals(expectedCurrency, getPersistedCurrency(expectedCurrency));
    }
}
