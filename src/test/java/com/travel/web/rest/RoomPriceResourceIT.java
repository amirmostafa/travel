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
import com.travel.domain.RoomPrice;
import com.travel.repository.RoomPriceRepository;
import com.travel.service.dto.RoomPriceDTO;
import com.travel.service.mapper.RoomPriceMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
