package com.travel.web.rest;

import com.travel.repository.TourPackageRepository;
import com.travel.service.TourPackageService;
import com.travel.service.dto.TourPackageDTO;
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
 * REST controller for managing {@link com.travel.domain.TourPackage}.
 */
@RestController
@RequestMapping("/api/tour-packages")
public class TourPackageResource {

    private static final Logger log = LoggerFactory.getLogger(TourPackageResource.class);

    private static final String ENTITY_NAME = "tourPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TourPackageService tourPackageService;

    private final TourPackageRepository tourPackageRepository;

    public TourPackageResource(TourPackageService tourPackageService, TourPackageRepository tourPackageRepository) {
        this.tourPackageService = tourPackageService;
        this.tourPackageRepository = tourPackageRepository;
    }

    /**
     * {@code POST  /tour-packages} : Create a new tourPackage.
     *
     * @param tourPackageDTO the tourPackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tourPackageDTO, or with status {@code 400 (Bad Request)} if the tourPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TourPackageDTO> createTourPackage(@Valid @RequestBody TourPackageDTO tourPackageDTO) throws URISyntaxException {
        log.debug("REST request to save TourPackage : {}", tourPackageDTO);
        if (tourPackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new tourPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tourPackageDTO = tourPackageService.save(tourPackageDTO);
        return ResponseEntity.created(new URI("/api/tour-packages/" + tourPackageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tourPackageDTO.getId().toString()))
            .body(tourPackageDTO);
    }

    /**
     * {@code PUT  /tour-packages/:id} : Updates an existing tourPackage.
     *
     * @param id the id of the tourPackageDTO to save.
     * @param tourPackageDTO the tourPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourPackageDTO,
     * or with status {@code 400 (Bad Request)} if the tourPackageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tourPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TourPackageDTO> updateTourPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TourPackageDTO tourPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TourPackage : {}, {}", id, tourPackageDTO);
        if (tourPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tourPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tourPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tourPackageDTO = tourPackageService.update(tourPackageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tourPackageDTO.getId().toString()))
            .body(tourPackageDTO);
    }

    /**
     * {@code PATCH  /tour-packages/:id} : Partial updates given fields of an existing tourPackage, field will ignore if it is null
     *
     * @param id the id of the tourPackageDTO to save.
     * @param tourPackageDTO the tourPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourPackageDTO,
     * or with status {@code 400 (Bad Request)} if the tourPackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tourPackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tourPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TourPackageDTO> partialUpdateTourPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TourPackageDTO tourPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TourPackage partially : {}, {}", id, tourPackageDTO);
        if (tourPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tourPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tourPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TourPackageDTO> result = tourPackageService.partialUpdate(tourPackageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tourPackageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tour-packages} : get all the tourPackages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tourPackages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TourPackageDTO>> getAllTourPackages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TourPackages");
        Page<TourPackageDTO> page = tourPackageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tour-packages/:id} : get the "id" tourPackage.
     *
     * @param id the id of the tourPackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tourPackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TourPackageDTO> getTourPackage(@PathVariable("id") Long id) {
        log.debug("REST request to get TourPackage : {}", id);
        Optional<TourPackageDTO> tourPackageDTO = tourPackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tourPackageDTO);
    }

    /**
     * {@code DELETE  /tour-packages/:id} : delete the "id" tourPackage.
     *
     * @param id the id of the tourPackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPackage(@PathVariable("id") Long id) {
        log.debug("REST request to delete TourPackage : {}", id);
        tourPackageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
