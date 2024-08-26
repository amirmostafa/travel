package com.travel.web.rest;

import static com.travel.domain.HotelAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.Hotel;
import com.travel.repository.HotelRepository;
import com.travel.service.dto.HotelDTO;
import com.travel.service.mapper.HotelMapper;
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
 * Integration tests for the {@link HotelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HotelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_STAR_RATING = 1;
    private static final Integer UPDATED_STAR_RATING = 2;

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "Y@yIf.gM";
    private static final String UPDATED_EMAIL = "]5!B`*@W&\".bFDku7";

    private static final String ENTITY_API_URL = "/api/hotels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHotelMockMvc;

    private Hotel hotel;

    private Hotel insertedHotel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hotel createEntity(EntityManager em) {
        Hotel hotel = new Hotel()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .starRating(DEFAULT_STAR_RATING)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .email(DEFAULT_EMAIL);
        return hotel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hotel createUpdatedEntity(EntityManager em) {
        Hotel hotel = new Hotel()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .starRating(UPDATED_STAR_RATING)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL);
        return hotel;
    }

    @BeforeEach
    public void initTest() {
        hotel = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHotel != null) {
            hotelRepository.delete(insertedHotel);
            insertedHotel = null;
        }
    }

    @Test
    @Transactional
    void createHotel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Hotel
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);
        var returnedHotelDTO = om.readValue(
            restHotelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HotelDTO.class
        );

        // Validate the Hotel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHotel = hotelMapper.toEntity(returnedHotelDTO);
        assertHotelUpdatableFieldsEquals(returnedHotel, getPersistedHotel(returnedHotel));

        insertedHotel = returnedHotel;
    }

    @Test
    @Transactional
    void createHotelWithExistingId() throws Exception {
        // Create the Hotel with an existing ID
        hotel.setId(1L);
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hotel.setName(null);

        // Create the Hotel, which fails.
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hotel.setAddress(null);

        // Create the Hotel, which fails.
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStarRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hotel.setStarRating(null);

        // Create the Hotel, which fails.
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hotel.setContactNumber(null);

        // Create the Hotel, which fails.
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hotel.setEmail(null);

        // Create the Hotel, which fails.
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHotels() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList
        restHotelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].starRating").value(hasItem(DEFAULT_STAR_RATING)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getHotel() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get the hotel
        restHotelMockMvc
            .perform(get(ENTITY_API_URL_ID, hotel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hotel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.starRating").value(DEFAULT_STAR_RATING))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingHotel() throws Exception {
        // Get the hotel
        restHotelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHotel() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hotel
        Hotel updatedHotel = hotelRepository.findById(hotel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHotel are not directly saved in db
        em.detach(updatedHotel);
        updatedHotel
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .starRating(UPDATED_STAR_RATING)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL);
        HotelDTO hotelDTO = hotelMapper.toDto(updatedHotel);

        restHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hotelDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHotelToMatchAllProperties(updatedHotel);
    }

    @Test
    @Transactional
    void putNonExistingHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hotel.setId(longCount.incrementAndGet());

        // Create the Hotel
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hotelDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hotel.setId(longCount.incrementAndGet());

        // Create the Hotel
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hotel.setId(longCount.incrementAndGet());

        // Create the Hotel
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHotelWithPatch() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hotel using partial update
        Hotel partialUpdatedHotel = new Hotel();
        partialUpdatedHotel.setId(hotel.getId());

        partialUpdatedHotel
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .starRating(UPDATED_STAR_RATING)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL);

        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHotel))
            )
            .andExpect(status().isOk());

        // Validate the Hotel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHotelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHotel, hotel), getPersistedHotel(hotel));
    }

    @Test
    @Transactional
    void fullUpdateHotelWithPatch() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hotel using partial update
        Hotel partialUpdatedHotel = new Hotel();
        partialUpdatedHotel.setId(hotel.getId());

        partialUpdatedHotel
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .starRating(UPDATED_STAR_RATING)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL);

        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHotel))
            )
            .andExpect(status().isOk());

        // Validate the Hotel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHotelUpdatableFieldsEquals(partialUpdatedHotel, getPersistedHotel(partialUpdatedHotel));
    }

    @Test
    @Transactional
    void patchNonExistingHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hotel.setId(longCount.incrementAndGet());

        // Create the Hotel
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hotelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hotel.setId(longCount.incrementAndGet());

        // Create the Hotel
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hotel.setId(longCount.incrementAndGet());

        // Create the Hotel
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHotel() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hotel
        restHotelMockMvc
            .perform(delete(ENTITY_API_URL_ID, hotel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hotelRepository.count();
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

    protected Hotel getPersistedHotel(Hotel hotel) {
        return hotelRepository.findById(hotel.getId()).orElseThrow();
    }

    protected void assertPersistedHotelToMatchAllProperties(Hotel expectedHotel) {
        assertHotelAllPropertiesEquals(expectedHotel, getPersistedHotel(expectedHotel));
    }

    protected void assertPersistedHotelToMatchUpdatableProperties(Hotel expectedHotel) {
        assertHotelAllUpdatablePropertiesEquals(expectedHotel, getPersistedHotel(expectedHotel));
    }
}
