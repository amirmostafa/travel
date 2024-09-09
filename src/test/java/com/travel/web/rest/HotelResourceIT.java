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
import com.travel.domain.Testimonial;
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
    private static final Integer SMALLER_STAR_RATING = 1 - 1;

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "Y@yIf.gM";
    private static final String UPDATED_EMAIL = "]5!B`*@W&\".bFDku7";

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

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
            .email(DEFAULT_EMAIL)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .cityCode(DEFAULT_CITY_CODE)
            .imageUrl(DEFAULT_IMAGE_URL);
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
            .email(UPDATED_EMAIL)
            .countryCode(UPDATED_COUNTRY_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .imageUrl(UPDATED_IMAGE_URL);
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
    void checkCountryCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hotel.setCountryCode(null);

        // Create the Hotel, which fails.
        HotelDTO hotelDTO = hotelMapper.toDto(hotel);

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hotelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hotel.setCityCode(null);

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
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));
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
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE))
            .andExpect(jsonPath("$.cityCode").value(DEFAULT_CITY_CODE))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getHotelsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        Long id = hotel.getId();

        defaultHotelFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHotelFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHotelFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHotelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where name equals to
        defaultHotelFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHotelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where name in
        defaultHotelFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHotelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where name is not null
        defaultHotelFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where name contains
        defaultHotelFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHotelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where name does not contain
        defaultHotelFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHotelsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where address equals to
        defaultHotelFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHotelsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where address in
        defaultHotelFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHotelsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where address is not null
        defaultHotelFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where address contains
        defaultHotelFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHotelsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where address does not contain
        defaultHotelFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHotelsByStarRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where starRating equals to
        defaultHotelFiltering("starRating.equals=" + DEFAULT_STAR_RATING, "starRating.equals=" + UPDATED_STAR_RATING);
    }

    @Test
    @Transactional
    void getAllHotelsByStarRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where starRating in
        defaultHotelFiltering("starRating.in=" + DEFAULT_STAR_RATING + "," + UPDATED_STAR_RATING, "starRating.in=" + UPDATED_STAR_RATING);
    }

    @Test
    @Transactional
    void getAllHotelsByStarRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where starRating is not null
        defaultHotelFiltering("starRating.specified=true", "starRating.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByStarRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where starRating is greater than or equal to
        defaultHotelFiltering(
            "starRating.greaterThanOrEqual=" + DEFAULT_STAR_RATING,
            "starRating.greaterThanOrEqual=" + (DEFAULT_STAR_RATING + 1)
        );
    }

    @Test
    @Transactional
    void getAllHotelsByStarRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where starRating is less than or equal to
        defaultHotelFiltering("starRating.lessThanOrEqual=" + DEFAULT_STAR_RATING, "starRating.lessThanOrEqual=" + SMALLER_STAR_RATING);
    }

    @Test
    @Transactional
    void getAllHotelsByStarRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where starRating is less than
        defaultHotelFiltering("starRating.lessThan=" + (DEFAULT_STAR_RATING + 1), "starRating.lessThan=" + DEFAULT_STAR_RATING);
    }

    @Test
    @Transactional
    void getAllHotelsByStarRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where starRating is greater than
        defaultHotelFiltering("starRating.greaterThan=" + SMALLER_STAR_RATING, "starRating.greaterThan=" + DEFAULT_STAR_RATING);
    }

    @Test
    @Transactional
    void getAllHotelsByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where contactNumber equals to
        defaultHotelFiltering("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER, "contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllHotelsByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where contactNumber in
        defaultHotelFiltering(
            "contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER,
            "contactNumber.in=" + UPDATED_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllHotelsByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where contactNumber is not null
        defaultHotelFiltering("contactNumber.specified=true", "contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where contactNumber contains
        defaultHotelFiltering("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER, "contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllHotelsByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where contactNumber does not contain
        defaultHotelFiltering(
            "contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER,
            "contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllHotelsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where email equals to
        defaultHotelFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHotelsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where email in
        defaultHotelFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHotelsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where email is not null
        defaultHotelFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where email contains
        defaultHotelFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHotelsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where email does not contain
        defaultHotelFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllHotelsByCountryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where countryCode equals to
        defaultHotelFiltering("countryCode.equals=" + DEFAULT_COUNTRY_CODE, "countryCode.equals=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    void getAllHotelsByCountryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where countryCode in
        defaultHotelFiltering(
            "countryCode.in=" + DEFAULT_COUNTRY_CODE + "," + UPDATED_COUNTRY_CODE,
            "countryCode.in=" + UPDATED_COUNTRY_CODE
        );
    }

    @Test
    @Transactional
    void getAllHotelsByCountryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where countryCode is not null
        defaultHotelFiltering("countryCode.specified=true", "countryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByCountryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where countryCode contains
        defaultHotelFiltering("countryCode.contains=" + DEFAULT_COUNTRY_CODE, "countryCode.contains=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    void getAllHotelsByCountryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where countryCode does not contain
        defaultHotelFiltering("countryCode.doesNotContain=" + UPDATED_COUNTRY_CODE, "countryCode.doesNotContain=" + DEFAULT_COUNTRY_CODE);
    }

    @Test
    @Transactional
    void getAllHotelsByCityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cityCode equals to
        defaultHotelFiltering("cityCode.equals=" + DEFAULT_CITY_CODE, "cityCode.equals=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllHotelsByCityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cityCode in
        defaultHotelFiltering("cityCode.in=" + DEFAULT_CITY_CODE + "," + UPDATED_CITY_CODE, "cityCode.in=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllHotelsByCityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cityCode is not null
        defaultHotelFiltering("cityCode.specified=true", "cityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByCityCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cityCode contains
        defaultHotelFiltering("cityCode.contains=" + DEFAULT_CITY_CODE, "cityCode.contains=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllHotelsByCityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cityCode does not contain
        defaultHotelFiltering("cityCode.doesNotContain=" + UPDATED_CITY_CODE, "cityCode.doesNotContain=" + DEFAULT_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllHotelsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where imageUrl equals to
        defaultHotelFiltering("imageUrl.equals=" + DEFAULT_IMAGE_URL, "imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHotelsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where imageUrl in
        defaultHotelFiltering("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL, "imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHotelsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where imageUrl is not null
        defaultHotelFiltering("imageUrl.specified=true", "imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllHotelsByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where imageUrl contains
        defaultHotelFiltering("imageUrl.contains=" + DEFAULT_IMAGE_URL, "imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHotelsByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHotel = hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where imageUrl does not contain
        defaultHotelFiltering("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL, "imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHotelsByTestimonialIsEqualToSomething() throws Exception {
        Testimonial testimonial;
        if (TestUtil.findAll(em, Testimonial.class).isEmpty()) {
            hotelRepository.saveAndFlush(hotel);
            testimonial = TestimonialResourceIT.createEntity(em);
        } else {
            testimonial = TestUtil.findAll(em, Testimonial.class).get(0);
        }
        em.persist(testimonial);
        em.flush();
        hotel.setTestimonial(testimonial);
        hotelRepository.saveAndFlush(hotel);
        Long testimonialId = testimonial.getId();
        // Get all the hotelList where testimonial equals to testimonialId
        defaultHotelShouldBeFound("testimonialId.equals=" + testimonialId);

        // Get all the hotelList where testimonial equals to (testimonialId + 1)
        defaultHotelShouldNotBeFound("testimonialId.equals=" + (testimonialId + 1));
    }

    private void defaultHotelFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHotelShouldBeFound(shouldBeFound);
        defaultHotelShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHotelShouldBeFound(String filter) throws Exception {
        restHotelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].starRating").value(hasItem(DEFAULT_STAR_RATING)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));

        // Check, that the count call also returns 1
        restHotelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHotelShouldNotBeFound(String filter) throws Exception {
        restHotelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHotelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .email(UPDATED_EMAIL)
            .countryCode(UPDATED_COUNTRY_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .imageUrl(UPDATED_IMAGE_URL);
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
            .email(UPDATED_EMAIL)
            .countryCode(UPDATED_COUNTRY_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .imageUrl(UPDATED_IMAGE_URL);

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
