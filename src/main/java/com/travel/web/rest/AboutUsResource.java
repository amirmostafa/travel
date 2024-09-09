package com.travel.web.rest;

import com.travel.repository.AboutUsRepository;
import com.travel.service.AboutUsQueryService;
import com.travel.service.AboutUsService;
import com.travel.service.criteria.AboutUsCriteria;
import com.travel.service.dto.AboutUsDTO;
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
 * REST controller for managing {@link com.travel.domain.AboutUs}.
 */
@RestController
@RequestMapping("/api/aboutuses")
public class AboutUsResource {

    private static final Logger log = LoggerFactory.getLogger(AboutUsResource.class);

    private static final String ENTITY_NAME = "aboutUs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AboutUsService aboutUsService;

    private final AboutUsRepository aboutUsRepository;

    private final AboutUsQueryService aboutUsQueryService;

    public AboutUsResource(AboutUsService aboutUsService, AboutUsRepository aboutUsRepository, AboutUsQueryService aboutUsQueryService) {
        this.aboutUsService = aboutUsService;
        this.aboutUsRepository = aboutUsRepository;
        this.aboutUsQueryService = aboutUsQueryService;
    }

    /**
     * {@code POST  /aboutuses} : Create a new aboutUs.
     *
     * @param aboutUsDTO the aboutUsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aboutUsDTO, or with status {@code 400 (Bad Request)} if the aboutUs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AboutUsDTO> createAboutUs(@Valid @RequestBody AboutUsDTO aboutUsDTO) throws URISyntaxException {
        log.debug("REST request to save AboutUs : {}", aboutUsDTO);
        if (aboutUsDTO.getId() != null) {
            throw new BadRequestAlertException("A new aboutUs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aboutUsDTO = aboutUsService.save(aboutUsDTO);
        return ResponseEntity.created(new URI("/api/aboutuses/" + aboutUsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aboutUsDTO.getId().toString()))
            .body(aboutUsDTO);
    }

    /**
     * {@code PUT  /aboutuses/:id} : Updates an existing aboutUs.
     *
     * @param id the id of the aboutUsDTO to save.
     * @param aboutUsDTO the aboutUsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutUsDTO,
     * or with status {@code 400 (Bad Request)} if the aboutUsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aboutUsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AboutUsDTO> updateAboutUs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AboutUsDTO aboutUsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AboutUs : {}, {}", id, aboutUsDTO);
        if (aboutUsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutUsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutUsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aboutUsDTO = aboutUsService.update(aboutUsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutUsDTO.getId().toString()))
            .body(aboutUsDTO);
    }

    /**
     * {@code PATCH  /aboutuses/:id} : Partial updates given fields of an existing aboutUs, field will ignore if it is null
     *
     * @param id the id of the aboutUsDTO to save.
     * @param aboutUsDTO the aboutUsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutUsDTO,
     * or with status {@code 400 (Bad Request)} if the aboutUsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aboutUsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aboutUsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AboutUsDTO> partialUpdateAboutUs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AboutUsDTO aboutUsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AboutUs partially : {}, {}", id, aboutUsDTO);
        if (aboutUsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutUsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutUsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AboutUsDTO> result = aboutUsService.partialUpdate(aboutUsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutUsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aboutuses} : get all the aboutuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aboutuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AboutUsDTO>> getAllAboutuses(
        AboutUsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Aboutuses by criteria: {}", criteria);

        Page<AboutUsDTO> page = aboutUsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aboutuses/count} : count all the aboutuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAboutuses(AboutUsCriteria criteria) {
        log.debug("REST request to count Aboutuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(aboutUsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /aboutuses/:id} : get the "id" aboutUs.
     *
     * @param id the id of the aboutUsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aboutUsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AboutUsDTO> getAboutUs(@PathVariable("id") Long id) {
        log.debug("REST request to get AboutUs : {}", id);
        Optional<AboutUsDTO> aboutUsDTO = aboutUsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aboutUsDTO);
    }

    /**
     * {@code DELETE  /aboutuses/:id} : delete the "id" aboutUs.
     *
     * @param id the id of the aboutUsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAboutUs(@PathVariable("id") Long id) {
        log.debug("REST request to delete AboutUs : {}", id);
        aboutUsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
