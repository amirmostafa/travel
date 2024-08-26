package com.travel.web.rest;

import static com.travel.domain.TourPackageAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.travel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.TourPackage;
import com.travel.repository.TourPackageRepository;
import com.travel.service.dto.TourPackageDTO;
import com.travel.service.mapper.TourPackageMapper;
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
 * Integration tests for the {@link TourPackageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TourPackageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final Integer DEFAULT_DURATION_DAYS = 1;
    private static final Integer UPDATED_DURATION_DAYS = 2;

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final String ENTITY_API_URL = "/api/tour-packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private TourPackageMapper tourPackageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTourPackageMockMvc;

    private TourPackage tourPackage;

    private TourPackage insertedTourPackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourPackage createEntity(EntityManager em) {
        TourPackage tourPackage = new TourPackage()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .durationDays(DEFAULT_DURATION_DAYS)
            .available(DEFAULT_AVAILABLE);
        return tourPackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourPackage createUpdatedEntity(EntityManager em) {
        TourPackage tourPackage = new TourPackage()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .durationDays(UPDATED_DURATION_DAYS)
            .available(UPDATED_AVAILABLE);
        return tourPackage;
    }

    @BeforeEach
    public void initTest() {
        tourPackage = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTourPackage != null) {
            tourPackageRepository.delete(insertedTourPackage);
            insertedTourPackage = null;
        }
    }

    @Test
    @Transactional
    void createTourPackage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TourPackage
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);
        var returnedTourPackageDTO = om.readValue(
            restTourPackageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TourPackageDTO.class
        );

        // Validate the TourPackage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTourPackage = tourPackageMapper.toEntity(returnedTourPackageDTO);
        assertTourPackageUpdatableFieldsEquals(returnedTourPackage, getPersistedTourPackage(returnedTourPackage));

        insertedTourPackage = returnedTourPackage;
    }

    @Test
    @Transactional
    void createTourPackageWithExistingId() throws Exception {
        // Create the TourPackage with an existing ID
        tourPackage.setId(1L);
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTourPackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tourPackage.setName(null);

        // Create the TourPackage, which fails.
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        restTourPackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tourPackage.setDescription(null);

        // Create the TourPackage, which fails.
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        restTourPackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tourPackage.setPrice(null);

        // Create the TourPackage, which fails.
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        restTourPackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationDaysIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tourPackage.setDurationDays(null);

        // Create the TourPackage, which fails.
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        restTourPackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tourPackage.setAvailable(null);

        // Create the TourPackage, which fails.
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        restTourPackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTourPackages() throws Exception {
        // Initialize the database
        insertedTourPackage = tourPackageRepository.saveAndFlush(tourPackage);

        // Get all the tourPackageList
        restTourPackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tourPackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].durationDays").value(hasItem(DEFAULT_DURATION_DAYS)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getTourPackage() throws Exception {
        // Initialize the database
        insertedTourPackage = tourPackageRepository.saveAndFlush(tourPackage);

        // Get the tourPackage
        restTourPackageMockMvc
            .perform(get(ENTITY_API_URL_ID, tourPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tourPackage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.durationDays").value(DEFAULT_DURATION_DAYS))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTourPackage() throws Exception {
        // Get the tourPackage
        restTourPackageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTourPackage() throws Exception {
        // Initialize the database
        insertedTourPackage = tourPackageRepository.saveAndFlush(tourPackage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tourPackage
        TourPackage updatedTourPackage = tourPackageRepository.findById(tourPackage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTourPackage are not directly saved in db
        em.detach(updatedTourPackage);
        updatedTourPackage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .durationDays(UPDATED_DURATION_DAYS)
            .available(UPDATED_AVAILABLE);
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(updatedTourPackage);

        restTourPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tourPackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tourPackageDTO))
            )
            .andExpect(status().isOk());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTourPackageToMatchAllProperties(updatedTourPackage);
    }

    @Test
    @Transactional
    void putNonExistingTourPackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tourPackage.setId(longCount.incrementAndGet());

        // Create the TourPackage
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTourPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tourPackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tourPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTourPackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tourPackage.setId(longCount.incrementAndGet());

        // Create the TourPackage
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tourPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTourPackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tourPackage.setId(longCount.incrementAndGet());

        // Create the TourPackage
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourPackageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTourPackageWithPatch() throws Exception {
        // Initialize the database
        insertedTourPackage = tourPackageRepository.saveAndFlush(tourPackage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tourPackage using partial update
        TourPackage partialUpdatedTourPackage = new TourPackage();
        partialUpdatedTourPackage.setId(tourPackage.getId());

        partialUpdatedTourPackage.name(UPDATED_NAME);

        restTourPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTourPackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTourPackage))
            )
            .andExpect(status().isOk());

        // Validate the TourPackage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTourPackageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTourPackage, tourPackage),
            getPersistedTourPackage(tourPackage)
        );
    }

    @Test
    @Transactional
    void fullUpdateTourPackageWithPatch() throws Exception {
        // Initialize the database
        insertedTourPackage = tourPackageRepository.saveAndFlush(tourPackage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tourPackage using partial update
        TourPackage partialUpdatedTourPackage = new TourPackage();
        partialUpdatedTourPackage.setId(tourPackage.getId());

        partialUpdatedTourPackage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .durationDays(UPDATED_DURATION_DAYS)
            .available(UPDATED_AVAILABLE);

        restTourPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTourPackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTourPackage))
            )
            .andExpect(status().isOk());

        // Validate the TourPackage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTourPackageUpdatableFieldsEquals(partialUpdatedTourPackage, getPersistedTourPackage(partialUpdatedTourPackage));
    }

    @Test
    @Transactional
    void patchNonExistingTourPackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tourPackage.setId(longCount.incrementAndGet());

        // Create the TourPackage
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTourPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tourPackageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tourPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTourPackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tourPackage.setId(longCount.incrementAndGet());

        // Create the TourPackage
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tourPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTourPackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tourPackage.setId(longCount.incrementAndGet());

        // Create the TourPackage
        TourPackageDTO tourPackageDTO = tourPackageMapper.toDto(tourPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourPackageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tourPackageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TourPackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTourPackage() throws Exception {
        // Initialize the database
        insertedTourPackage = tourPackageRepository.saveAndFlush(tourPackage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tourPackage
        restTourPackageMockMvc
            .perform(delete(ENTITY_API_URL_ID, tourPackage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tourPackageRepository.count();
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

    protected TourPackage getPersistedTourPackage(TourPackage tourPackage) {
        return tourPackageRepository.findById(tourPackage.getId()).orElseThrow();
    }

    protected void assertPersistedTourPackageToMatchAllProperties(TourPackage expectedTourPackage) {
        assertTourPackageAllPropertiesEquals(expectedTourPackage, getPersistedTourPackage(expectedTourPackage));
    }

    protected void assertPersistedTourPackageToMatchUpdatableProperties(TourPackage expectedTourPackage) {
        assertTourPackageAllUpdatablePropertiesEquals(expectedTourPackage, getPersistedTourPackage(expectedTourPackage));
    }
}
