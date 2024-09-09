package com.travel.web.rest;

import com.travel.repository.TestimonialRepository;
import com.travel.service.TestimonialQueryService;
import com.travel.service.TestimonialService;
import com.travel.service.criteria.TestimonialCriteria;
import com.travel.service.dto.TestimonialDTO;
import com.travel.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.travel.domain.Testimonial}.
 */
@RestController
@RequestMapping("/api/testimonials")
public class TestimonialResource {

    private static final Logger log = LoggerFactory.getLogger(TestimonialResource.class);

    private static final String ENTITY_NAME = "testimonial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestimonialService testimonialService;

    private final TestimonialRepository testimonialRepository;

    private final TestimonialQueryService testimonialQueryService;

    public TestimonialResource(
        TestimonialService testimonialService,
        TestimonialRepository testimonialRepository,
        TestimonialQueryService testimonialQueryService
    ) {
        this.testimonialService = testimonialService;
        this.testimonialRepository = testimonialRepository;
        this.testimonialQueryService = testimonialQueryService;
    }

    /**
     * {@code POST  /testimonials} : Create a new testimonial.
     *
     * @param testimonialDTO the testimonialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testimonialDTO, or with status {@code 400 (Bad Request)} if the testimonial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestimonialDTO> createTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) throws URISyntaxException {
        log.debug("REST request to save Testimonial : {}", testimonialDTO);
        if (testimonialDTO.getId() != null) {
            throw new BadRequestAlertException("A new testimonial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testimonialDTO = testimonialService.save(testimonialDTO);
        return ResponseEntity.created(new URI("/api/testimonials/" + testimonialDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, testimonialDTO.getId().toString()))
            .body(testimonialDTO);
    }

    /**
     * {@code PUT  /testimonials/:id} : Updates an existing testimonial.
     *
     * @param id the id of the testimonialDTO to save.
     * @param testimonialDTO the testimonialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testimonialDTO,
     * or with status {@code 400 (Bad Request)} if the testimonialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testimonialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestimonialDTO> updateTestimonial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestimonialDTO testimonialDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Testimonial : {}, {}", id, testimonialDTO);
        if (testimonialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testimonialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testimonialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        testimonialDTO = testimonialService.update(testimonialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testimonialDTO.getId().toString()))
            .body(testimonialDTO);
    }

    /**
     * {@code PATCH  /testimonials/:id} : Partial updates given fields of an existing testimonial, field will ignore if it is null
     *
     * @param id the id of the testimonialDTO to save.
     * @param testimonialDTO the testimonialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testimonialDTO,
     * or with status {@code 400 (Bad Request)} if the testimonialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testimonialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testimonialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestimonialDTO> partialUpdateTestimonial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestimonialDTO testimonialDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Testimonial partially : {}, {}", id, testimonialDTO);
        if (testimonialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testimonialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testimonialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestimonialDTO> result = testimonialService.partialUpdate(testimonialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testimonialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /testimonials} : get all the testimonials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testimonials in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TestimonialDTO>> getAllTestimonials(
        TestimonialCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Testimonials by criteria: {}", criteria);

        Page<TestimonialDTO> page = testimonialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /testimonials/count} : count all the testimonials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTestimonials(TestimonialCriteria criteria) {
        log.debug("REST request to count Testimonials by criteria: {}", criteria);
        return ResponseEntity.ok().body(testimonialQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /testimonials/:id} : get the "id" testimonial.
     *
     * @param id the id of the testimonialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testimonialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestimonialDTO> getTestimonial(@PathVariable("id") Long id) {
        log.debug("REST request to get Testimonial : {}", id);
        Optional<TestimonialDTO> testimonialDTO = testimonialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testimonialDTO);
    }

    /**
     * {@code DELETE  /testimonials/:id} : delete the "id" testimonial.
     *
     * @param id the id of the testimonialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable("id") Long id) {
        log.debug("REST request to delete Testimonial : {}", id);
        testimonialService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
