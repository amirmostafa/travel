package com.travel.web.rest;

import static com.travel.domain.RoomPriceAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.travel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.Room;
import com.travel.domain.RoomPrice;
import com.travel.repository.RoomPriceRepository;
import com.travel.service.dto.RoomPriceDTO;
import com.travel.service.mapper.RoomPriceMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link RoomPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomPriceResourceIT {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(0 - 1);

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TO_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/room-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoomPriceRepository roomPriceRepository;

    @Autowired
    private RoomPriceMapper roomPriceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomPriceMockMvc;

    private RoomPrice roomPrice;

    private RoomPrice insertedRoomPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomPrice createEntity(EntityManager em) {
        RoomPrice roomPrice = new RoomPrice().price(DEFAULT_PRICE).fromDate(DEFAULT_FROM_DATE).toDate(DEFAULT_TO_DATE);
        return roomPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomPrice createUpdatedEntity(EntityManager em) {
        RoomPrice roomPrice = new RoomPrice().price(UPDATED_PRICE).fromDate(UPDATED_FROM_DATE).toDate(UPDATED_TO_DATE);
        return roomPrice;
    }

    @BeforeEach
    public void initTest() {
        roomPrice = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRoomPrice != null) {
            roomPriceRepository.delete(insertedRoomPrice);
            insertedRoomPrice = null;
        }
    }

    @Test
    @Transactional
    void createRoomPrice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RoomPrice
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);
        var returnedRoomPriceDTO = om.readValue(
            restRoomPriceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomPriceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoomPriceDTO.class
        );

        // Validate the RoomPrice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoomPrice = roomPriceMapper.toEntity(returnedRoomPriceDTO);
        assertRoomPriceUpdatableFieldsEquals(returnedRoomPrice, getPersistedRoomPrice(returnedRoomPrice));

        insertedRoomPrice = returnedRoomPrice;
    }

    @Test
    @Transactional
    void createRoomPriceWithExistingId() throws Exception {
        // Create the RoomPrice with an existing ID
        roomPrice.setId(1L);
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomPriceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roomPrice.setPrice(null);

        // Create the RoomPrice, which fails.
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        restRoomPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomPriceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roomPrice.setFromDate(null);

        // Create the RoomPrice, which fails.
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        restRoomPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomPriceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roomPrice.setToDate(null);

        // Create the RoomPrice, which fails.
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        restRoomPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomPriceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoomPrices() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList
        restRoomPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    void getRoomPrice() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get the roomPrice
        restRoomPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, roomPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomPrice.getId().intValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()));
    }

    @Test
    @Transactional
    void getRoomPricesByIdFiltering() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        Long id = roomPrice.getId();

        defaultRoomPriceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRoomPriceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRoomPriceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoomPricesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where price equals to
        defaultRoomPriceFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where price in
        defaultRoomPriceFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where price is not null
        defaultRoomPriceFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomPricesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where price is greater than or equal to
        defaultRoomPriceFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where price is less than or equal to
        defaultRoomPriceFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where price is less than
        defaultRoomPriceFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where price is greater than
        defaultRoomPriceFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where fromDate equals to
        defaultRoomPriceFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where fromDate in
        defaultRoomPriceFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where fromDate is not null
        defaultRoomPriceFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomPricesByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where fromDate is greater than or equal to
        defaultRoomPriceFiltering("fromDate.greaterThanOrEqual=" + DEFAULT_FROM_DATE, "fromDate.greaterThanOrEqual=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where fromDate is less than or equal to
        defaultRoomPriceFiltering("fromDate.lessThanOrEqual=" + DEFAULT_FROM_DATE, "fromDate.lessThanOrEqual=" + SMALLER_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where fromDate is less than
        defaultRoomPriceFiltering("fromDate.lessThan=" + UPDATED_FROM_DATE, "fromDate.lessThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where fromDate is greater than
        defaultRoomPriceFiltering("fromDate.greaterThan=" + SMALLER_FROM_DATE, "fromDate.greaterThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where toDate equals to
        defaultRoomPriceFiltering("toDate.equals=" + DEFAULT_TO_DATE, "toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where toDate in
        defaultRoomPriceFiltering("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE, "toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where toDate is not null
        defaultRoomPriceFiltering("toDate.specified=true", "toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomPricesByToDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where toDate is greater than or equal to
        defaultRoomPriceFiltering("toDate.greaterThanOrEqual=" + DEFAULT_TO_DATE, "toDate.greaterThanOrEqual=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByToDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where toDate is less than or equal to
        defaultRoomPriceFiltering("toDate.lessThanOrEqual=" + DEFAULT_TO_DATE, "toDate.lessThanOrEqual=" + SMALLER_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByToDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where toDate is less than
        defaultRoomPriceFiltering("toDate.lessThan=" + UPDATED_TO_DATE, "toDate.lessThan=" + DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByToDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        // Get all the roomPriceList where toDate is greater than
        defaultRoomPriceFiltering("toDate.greaterThan=" + SMALLER_TO_DATE, "toDate.greaterThan=" + DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRoomPricesByRoomIsEqualToSomething() throws Exception {
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            roomPriceRepository.saveAndFlush(roomPrice);
            room = RoomResourceIT.createEntity(em);
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        em.persist(room);
        em.flush();
        roomPrice.setRoom(room);
        roomPriceRepository.saveAndFlush(roomPrice);
        Long roomId = room.getId();
        // Get all the roomPriceList where room equals to roomId
        defaultRoomPriceShouldBeFound("roomId.equals=" + roomId);

        // Get all the roomPriceList where room equals to (roomId + 1)
        defaultRoomPriceShouldNotBeFound("roomId.equals=" + (roomId + 1));
    }

    private void defaultRoomPriceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRoomPriceShouldBeFound(shouldBeFound);
        defaultRoomPriceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoomPriceShouldBeFound(String filter) throws Exception {
        restRoomPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));

        // Check, that the count call also returns 1
        restRoomPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoomPriceShouldNotBeFound(String filter) throws Exception {
        restRoomPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoomPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoomPrice() throws Exception {
        // Get the roomPrice
        restRoomPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomPrice() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomPrice
        RoomPrice updatedRoomPrice = roomPriceRepository.findById(roomPrice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoomPrice are not directly saved in db
        em.detach(updatedRoomPrice);
        updatedRoomPrice.price(UPDATED_PRICE).fromDate(UPDATED_FROM_DATE).toDate(UPDATED_TO_DATE);
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(updatedRoomPrice);

        restRoomPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomPriceDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoomPriceToMatchAllProperties(updatedRoomPrice);
    }

    @Test
    @Transactional
    void putNonExistingRoomPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomPrice.setId(longCount.incrementAndGet());

        // Create the RoomPrice
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomPrice.setId(longCount.incrementAndGet());

        // Create the RoomPrice
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomPrice.setId(longCount.incrementAndGet());

        // Create the RoomPrice
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomPriceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomPriceWithPatch() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomPrice using partial update
        RoomPrice partialUpdatedRoomPrice = new RoomPrice();
        partialUpdatedRoomPrice.setId(roomPrice.getId());

        partialUpdatedRoomPrice.price(UPDATED_PRICE).fromDate(UPDATED_FROM_DATE);

        restRoomPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoomPrice))
            )
            .andExpect(status().isOk());

        // Validate the RoomPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomPriceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRoomPrice, roomPrice),
            getPersistedRoomPrice(roomPrice)
        );
    }

    @Test
    @Transactional
    void fullUpdateRoomPriceWithPatch() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomPrice using partial update
        RoomPrice partialUpdatedRoomPrice = new RoomPrice();
        partialUpdatedRoomPrice.setId(roomPrice.getId());

        partialUpdatedRoomPrice.price(UPDATED_PRICE).fromDate(UPDATED_FROM_DATE).toDate(UPDATED_TO_DATE);

        restRoomPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoomPrice))
            )
            .andExpect(status().isOk());

        // Validate the RoomPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomPriceUpdatableFieldsEquals(partialUpdatedRoomPrice, getPersistedRoomPrice(partialUpdatedRoomPrice));
    }

    @Test
    @Transactional
    void patchNonExistingRoomPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomPrice.setId(longCount.incrementAndGet());

        // Create the RoomPrice
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomPriceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomPrice.setId(longCount.incrementAndGet());

        // Create the RoomPrice
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomPrice.setId(longCount.incrementAndGet());

        // Create the RoomPrice
        RoomPriceDTO roomPriceDTO = roomPriceMapper.toDto(roomPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomPriceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomPriceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomPrice() throws Exception {
        // Initialize the database
        insertedRoomPrice = roomPriceRepository.saveAndFlush(roomPrice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roomPrice
        restRoomPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roomPriceRepository.count();
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

    protected RoomPrice getPersistedRoomPrice(RoomPrice roomPrice) {
        return roomPriceRepository.findById(roomPrice.getId()).orElseThrow();
    }

    protected void assertPersistedRoomPriceToMatchAllProperties(RoomPrice expectedRoomPrice) {
        assertRoomPriceAllPropertiesEquals(expectedRoomPrice, getPersistedRoomPrice(expectedRoomPrice));
    }

    protected void assertPersistedRoomPriceToMatchUpdatableProperties(RoomPrice expectedRoomPrice) {
        assertRoomPriceAllUpdatablePropertiesEquals(expectedRoomPrice, getPersistedRoomPrice(expectedRoomPrice));
    }
}
