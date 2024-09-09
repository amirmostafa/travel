package com.travel.web.rest;

import static com.travel.domain.RoomAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.Hotel;
import com.travel.domain.Room;
import com.travel.domain.enumeration.RoomType;
import com.travel.repository.RoomRepository;
import com.travel.service.dto.RoomDTO;
import com.travel.service.mapper.RoomMapper;
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
 * Integration tests for the {@link RoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomResourceIT {

    private static final String DEFAULT_ROOM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NUMBER = "BBBBBBBBBB";

    private static final RoomType DEFAULT_TYPE = RoomType.SINGLE;
    private static final RoomType UPDATED_TYPE = RoomType.DOUBLE;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_DISCOUNT_PERCENTAGE = 0D;
    private static final Double UPDATED_DISCOUNT_PERCENTAGE = 1D;
    private static final Double SMALLER_DISCOUNT_PERCENTAGE = 0D - 1D;

    private static final String ENTITY_API_URL = "/api/rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    private Room insertedRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room()
            .roomNumber(DEFAULT_ROOM_NUMBER)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE);
        return room;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room()
            .roomNumber(UPDATED_ROOM_NUMBER)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRoom != null) {
            roomRepository.delete(insertedRoom);
            insertedRoom = null;
        }
    }

    @Test
    @Transactional
    void createRoom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);
        var returnedRoomDTO = om.readValue(
            restRoomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoomDTO.class
        );

        // Validate the Room in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoom = roomMapper.toEntity(returnedRoomDTO);
        assertRoomUpdatableFieldsEquals(returnedRoom, getPersistedRoom(returnedRoom));

        insertedRoom = returnedRoom;
    }

    @Test
    @Transactional
    void createRoomWithExistingId() throws Exception {
        // Create the Room with an existing ID
        room.setId(1L);
        RoomDTO roomDTO = roomMapper.toDto(room);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoomNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        room.setRoomNumber(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        room.setType(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRooms() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())));
    }

    @Test
    @Transactional
    void getRoom() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.roomNumber").value(DEFAULT_ROOM_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()));
    }

    @Test
    @Transactional
    void getRoomsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        Long id = room.getId();

        defaultRoomFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRoomFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRoomFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber equals to
        defaultRoomFiltering("roomNumber.equals=" + DEFAULT_ROOM_NUMBER, "roomNumber.equals=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber in
        defaultRoomFiltering("roomNumber.in=" + DEFAULT_ROOM_NUMBER + "," + UPDATED_ROOM_NUMBER, "roomNumber.in=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber is not null
        defaultRoomFiltering("roomNumber.specified=true", "roomNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber contains
        defaultRoomFiltering("roomNumber.contains=" + DEFAULT_ROOM_NUMBER, "roomNumber.contains=" + UPDATED_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomNumber does not contain
        defaultRoomFiltering("roomNumber.doesNotContain=" + UPDATED_ROOM_NUMBER, "roomNumber.doesNotContain=" + DEFAULT_ROOM_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoomsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where type equals to
        defaultRoomFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where type in
        defaultRoomFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where type is not null
        defaultRoomFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where description equals to
        defaultRoomFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where description in
        defaultRoomFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where description is not null
        defaultRoomFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where description contains
        defaultRoomFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where description does not contain
        defaultRoomFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDiscountPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where discountPercentage equals to
        defaultRoomFiltering(
            "discountPercentage.equals=" + DEFAULT_DISCOUNT_PERCENTAGE,
            "discountPercentage.equals=" + UPDATED_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllRoomsByDiscountPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where discountPercentage in
        defaultRoomFiltering(
            "discountPercentage.in=" + DEFAULT_DISCOUNT_PERCENTAGE + "," + UPDATED_DISCOUNT_PERCENTAGE,
            "discountPercentage.in=" + UPDATED_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllRoomsByDiscountPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where discountPercentage is not null
        defaultRoomFiltering("discountPercentage.specified=true", "discountPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByDiscountPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where discountPercentage is greater than or equal to
        defaultRoomFiltering(
            "discountPercentage.greaterThanOrEqual=" + DEFAULT_DISCOUNT_PERCENTAGE,
            "discountPercentage.greaterThanOrEqual=" + (DEFAULT_DISCOUNT_PERCENTAGE + 1)
        );
    }

    @Test
    @Transactional
    void getAllRoomsByDiscountPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where discountPercentage is less than or equal to
        defaultRoomFiltering(
            "discountPercentage.lessThanOrEqual=" + DEFAULT_DISCOUNT_PERCENTAGE,
            "discountPercentage.lessThanOrEqual=" + SMALLER_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllRoomsByDiscountPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where discountPercentage is less than
        defaultRoomFiltering(
            "discountPercentage.lessThan=" + (DEFAULT_DISCOUNT_PERCENTAGE + 1),
            "discountPercentage.lessThan=" + DEFAULT_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllRoomsByDiscountPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where discountPercentage is greater than
        defaultRoomFiltering(
            "discountPercentage.greaterThan=" + SMALLER_DISCOUNT_PERCENTAGE,
            "discountPercentage.greaterThan=" + DEFAULT_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllRoomsByHotelIsEqualToSomething() throws Exception {
        Hotel hotel;
        if (TestUtil.findAll(em, Hotel.class).isEmpty()) {
            roomRepository.saveAndFlush(room);
            hotel = HotelResourceIT.createEntity(em);
        } else {
            hotel = TestUtil.findAll(em, Hotel.class).get(0);
        }
        em.persist(hotel);
        em.flush();
        room.setHotel(hotel);
        roomRepository.saveAndFlush(room);
        Long hotelId = hotel.getId();
        // Get all the roomList where hotel equals to hotelId
        defaultRoomShouldBeFound("hotelId.equals=" + hotelId);

        // Get all the roomList where hotel equals to (hotelId + 1)
        defaultRoomShouldNotBeFound("hotelId.equals=" + (hotelId + 1));
    }

    private void defaultRoomFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRoomShouldBeFound(shouldBeFound);
        defaultRoomShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoomShouldBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNumber").value(hasItem(DEFAULT_ROOM_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())));

        // Check, that the count call also returns 1
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoomShouldNotBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoom() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom
            .roomNumber(UPDATED_ROOM_NUMBER)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);
        RoomDTO roomDTO = roomMapper.toDto(updatedRoom);

        restRoomMockMvc
            .perform(put(ENTITY_API_URL_ID, roomDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isOk());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoomToMatchAllProperties(updatedRoom);
    }

    @Test
    @Transactional
    void putNonExistingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL_ID, roomDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRoom, room), getPersistedRoom(room));
    }

    @Test
    @Transactional
    void fullUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom
            .roomNumber(UPDATED_ROOM_NUMBER)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomUpdatableFieldsEquals(partialUpdatedRoom, getPersistedRoom(partialUpdatedRoom));
    }

    @Test
    @Transactional
    void patchNonExistingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoom() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the room
        restRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, room.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roomRepository.count();
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

    protected Room getPersistedRoom(Room room) {
        return roomRepository.findById(room.getId()).orElseThrow();
    }

    protected void assertPersistedRoomToMatchAllProperties(Room expectedRoom) {
        assertRoomAllPropertiesEquals(expectedRoom, getPersistedRoom(expectedRoom));
    }

    protected void assertPersistedRoomToMatchUpdatableProperties(Room expectedRoom) {
        assertRoomAllUpdatablePropertiesEquals(expectedRoom, getPersistedRoom(expectedRoom));
    }
}
