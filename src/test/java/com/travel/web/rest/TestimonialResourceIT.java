package com.travel.web.rest;

import static com.travel.domain.TestimonialAsserts.*;
import static com.travel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.IntegrationTest;
import com.travel.domain.Testimonial;
import com.travel.repository.TestimonialRepository;
import com.travel.service.dto.TestimonialDTO;
import com.travel.service.mapper.TestimonialMapper;
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
 * Integration tests for the {@link TestimonialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestimonialResourceIT {

    private static final String DEFAULT_AUTHOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/testimonials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Autowired
    private TestimonialMapper testimonialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestimonialMockMvc;

    private Testimonial testimonial;

    private Testimonial insertedTestimonial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Testimonial createEntity(EntityManager em) {
        Testimonial testimonial = new Testimonial()
            .authorName(DEFAULT_AUTHOR_NAME)
            .content(DEFAULT_CONTENT)
            .rating(DEFAULT_RATING)
            .date(DEFAULT_DATE);
        return testimonial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Testimonial createUpdatedEntity(EntityManager em) {
        Testimonial testimonial = new Testimonial()
            .authorName(UPDATED_AUTHOR_NAME)
            .content(UPDATED_CONTENT)
            .rating(UPDATED_RATING)
            .date(UPDATED_DATE);
        return testimonial;
    }

    @BeforeEach
    public void initTest() {
        testimonial = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTestimonial != null) {
            testimonialRepository.delete(insertedTestimonial);
            insertedTestimonial = null;
        }
    }

    @Test
    @Transactional
    void createTestimonial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Testimonial
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);
        var returnedTestimonialDTO = om.readValue(
            restTestimonialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testimonialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TestimonialDTO.class
        );

        // Validate the Testimonial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTestimonial = testimonialMapper.toEntity(returnedTestimonialDTO);
        assertTestimonialUpdatableFieldsEquals(returnedTestimonial, getPersistedTestimonial(returnedTestimonial));

        insertedTestimonial = returnedTestimonial;
    }

    @Test
    @Transactional
    void createTestimonialWithExistingId() throws Exception {
        // Create the Testimonial with an existing ID
        testimonial.setId(1L);
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestimonialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testimonialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAuthorNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        testimonial.setAuthorName(null);

        // Create the Testimonial, which fails.
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        restTestimonialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testimonialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        testimonial.setContent(null);

        // Create the Testimonial, which fails.
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        restTestimonialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testimonialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        testimonial.setDate(null);

        // Create the Testimonial, which fails.
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        restTestimonialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testimonialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestimonials() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList
        restTestimonialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testimonial.getId().intValue())))
            .andExpect(jsonPath("$.[*].authorName").value(hasItem(DEFAULT_AUTHOR_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getTestimonial() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get the testimonial
        restTestimonialMockMvc
            .perform(get(ENTITY_API_URL_ID, testimonial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testimonial.getId().intValue()))
            .andExpect(jsonPath("$.authorName").value(DEFAULT_AUTHOR_NAME))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getTestimonialsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        Long id = testimonial.getId();

        defaultTestimonialFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTestimonialFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTestimonialFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestimonialsByAuthorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where authorName equals to
        defaultTestimonialFiltering("authorName.equals=" + DEFAULT_AUTHOR_NAME, "authorName.equals=" + UPDATED_AUTHOR_NAME);
    }

    @Test
    @Transactional
    void getAllTestimonialsByAuthorNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where authorName in
        defaultTestimonialFiltering(
            "authorName.in=" + DEFAULT_AUTHOR_NAME + "," + UPDATED_AUTHOR_NAME,
            "authorName.in=" + UPDATED_AUTHOR_NAME
        );
    }

    @Test
    @Transactional
    void getAllTestimonialsByAuthorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where authorName is not null
        defaultTestimonialFiltering("authorName.specified=true", "authorName.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByAuthorNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where authorName contains
        defaultTestimonialFiltering("authorName.contains=" + DEFAULT_AUTHOR_NAME, "authorName.contains=" + UPDATED_AUTHOR_NAME);
    }

    @Test
    @Transactional
    void getAllTestimonialsByAuthorNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where authorName does not contain
        defaultTestimonialFiltering("authorName.doesNotContain=" + UPDATED_AUTHOR_NAME, "authorName.doesNotContain=" + DEFAULT_AUTHOR_NAME);
    }

    @Test
    @Transactional
    void getAllTestimonialsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where content equals to
        defaultTestimonialFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where content in
        defaultTestimonialFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where content is not null
        defaultTestimonialFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where content contains
        defaultTestimonialFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where content does not contain
        defaultTestimonialFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where rating equals to
        defaultTestimonialFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where rating in
        defaultTestimonialFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where rating is not null
        defaultTestimonialFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where rating is greater than or equal to
        defaultTestimonialFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where rating is less than or equal to
        defaultTestimonialFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where rating is less than
        defaultTestimonialFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where rating is greater than
        defaultTestimonialFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where date equals to
        defaultTestimonialFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where date in
        defaultTestimonialFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where date is not null
        defaultTestimonialFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where date is greater than or equal to
        defaultTestimonialFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where date is less than or equal to
        defaultTestimonialFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where date is less than
        defaultTestimonialFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        // Get all the testimonialList where date is greater than
        defaultTestimonialFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    private void defaultTestimonialFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTestimonialShouldBeFound(shouldBeFound);
        defaultTestimonialShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestimonialShouldBeFound(String filter) throws Exception {
        restTestimonialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testimonial.getId().intValue())))
            .andExpect(jsonPath("$.[*].authorName").value(hasItem(DEFAULT_AUTHOR_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restTestimonialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestimonialShouldNotBeFound(String filter) throws Exception {
        restTestimonialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestimonialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestimonial() throws Exception {
        // Get the testimonial
        restTestimonialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestimonial() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testimonial
        Testimonial updatedTestimonial = testimonialRepository.findById(testimonial.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestimonial are not directly saved in db
        em.detach(updatedTestimonial);
        updatedTestimonial.authorName(UPDATED_AUTHOR_NAME).content(UPDATED_CONTENT).rating(UPDATED_RATING).date(UPDATED_DATE);
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(updatedTestimonial);

        restTestimonialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testimonialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testimonialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTestimonialToMatchAllProperties(updatedTestimonial);
    }

    @Test
    @Transactional
    void putNonExistingTestimonial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testimonial.setId(longCount.incrementAndGet());

        // Create the Testimonial
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestimonialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testimonialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testimonialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestimonial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testimonial.setId(longCount.incrementAndGet());

        // Create the Testimonial
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testimonialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestimonial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testimonial.setId(longCount.incrementAndGet());

        // Create the Testimonial
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testimonialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestimonialWithPatch() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testimonial using partial update
        Testimonial partialUpdatedTestimonial = new Testimonial();
        partialUpdatedTestimonial.setId(testimonial.getId());

        restTestimonialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestimonial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestimonial))
            )
            .andExpect(status().isOk());

        // Validate the Testimonial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestimonialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTestimonial, testimonial),
            getPersistedTestimonial(testimonial)
        );
    }

    @Test
    @Transactional
    void fullUpdateTestimonialWithPatch() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testimonial using partial update
        Testimonial partialUpdatedTestimonial = new Testimonial();
        partialUpdatedTestimonial.setId(testimonial.getId());

        partialUpdatedTestimonial.authorName(UPDATED_AUTHOR_NAME).content(UPDATED_CONTENT).rating(UPDATED_RATING).date(UPDATED_DATE);

        restTestimonialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestimonial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestimonial))
            )
            .andExpect(status().isOk());

        // Validate the Testimonial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestimonialUpdatableFieldsEquals(partialUpdatedTestimonial, getPersistedTestimonial(partialUpdatedTestimonial));
    }

    @Test
    @Transactional
    void patchNonExistingTestimonial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testimonial.setId(longCount.incrementAndGet());

        // Create the Testimonial
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestimonialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testimonialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testimonialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestimonial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testimonial.setId(longCount.incrementAndGet());

        // Create the Testimonial
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testimonialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestimonial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testimonial.setId(longCount.incrementAndGet());

        // Create the Testimonial
        TestimonialDTO testimonialDTO = testimonialMapper.toDto(testimonial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(testimonialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Testimonial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestimonial() throws Exception {
        // Initialize the database
        insertedTestimonial = testimonialRepository.saveAndFlush(testimonial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the testimonial
        restTestimonialMockMvc
            .perform(delete(ENTITY_API_URL_ID, testimonial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return testimonialRepository.count();
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

    protected Testimonial getPersistedTestimonial(Testimonial testimonial) {
        return testimonialRepository.findById(testimonial.getId()).orElseThrow();
    }

    protected void assertPersistedTestimonialToMatchAllProperties(Testimonial expectedTestimonial) {
        assertTestimonialAllPropertiesEquals(expectedTestimonial, getPersistedTestimonial(expectedTestimonial));
    }

    protected void assertPersistedTestimonialToMatchUpdatableProperties(Testimonial expectedTestimonial) {
        assertTestimonialAllUpdatablePropertiesEquals(expectedTestimonial, getPersistedTestimonial(expectedTestimonial));
    }
}
