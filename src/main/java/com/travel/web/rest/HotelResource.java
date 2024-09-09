package com.travel.web.rest;

import com.travel.repository.HotelRepository;
import com.travel.service.HotelQueryService;
import com.travel.service.HotelService;
import com.travel.service.criteria.HotelCriteria;
import com.travel.service.dto.HotelDTO;
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
 * REST controller for managing {@link com.travel.domain.Hotel}.
 */
@RestController
@RequestMapping("/api/hotels")
public class HotelResource {

    private static final Logger log = LoggerFactory.getLogger(HotelResource.class);

    private static final String ENTITY_NAME = "hotel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HotelService hotelService;

    private final HotelRepository hotelRepository;

    private final HotelQueryService hotelQueryService;

    public HotelResource(HotelService hotelService, HotelRepository hotelRepository, HotelQueryService hotelQueryService) {
        this.hotelService = hotelService;
        this.hotelRepository = hotelRepository;
        this.hotelQueryService = hotelQueryService;
    }

    /**
     * {@code POST  /hotels} : Create a new hotel.
     *
     * @param hotelDTO the hotelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hotelDTO, or with status {@code 400 (Bad Request)} if the hotel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HotelDTO> createHotel(@Valid @RequestBody HotelDTO hotelDTO) throws URISyntaxException {
        log.debug("REST request to save Hotel : {}", hotelDTO);
        if (hotelDTO.getId() != null) {
            throw new BadRequestAlertException("A new hotel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hotelDTO = hotelService.save(hotelDTO);
        return ResponseEntity.created(new URI("/api/hotels/" + hotelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hotelDTO.getId().toString()))
            .body(hotelDTO);
    }

    /**
     * {@code PUT  /hotels/:id} : Updates an existing hotel.
     *
     * @param id the id of the hotelDTO to save.
     * @param hotelDTO the hotelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hotelDTO,
     * or with status {@code 400 (Bad Request)} if the hotelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hotelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HotelDTO hotelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Hotel : {}, {}", id, hotelDTO);
        if (hotelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hotelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hotelDTO = hotelService.update(hotelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hotelDTO.getId().toString()))
            .body(hotelDTO);
    }

    /**
     * {@code PATCH  /hotels/:id} : Partial updates given fields of an existing hotel, field will ignore if it is null
     *
     * @param id the id of the hotelDTO to save.
     * @param hotelDTO the hotelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hotelDTO,
     * or with status {@code 400 (Bad Request)} if the hotelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hotelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hotelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HotelDTO> partialUpdateHotel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HotelDTO hotelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hotel partially : {}, {}", id, hotelDTO);
        if (hotelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hotelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HotelDTO> result = hotelService.partialUpdate(hotelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hotelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hotels} : get all the hotels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hotels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HotelDTO>> getAllHotels(
        HotelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Hotels by criteria: {}", criteria);

        Page<HotelDTO> page = hotelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hotels/count} : count all the hotels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHotels(HotelCriteria criteria) {
        log.debug("REST request to count Hotels by criteria: {}", criteria);
        return ResponseEntity.ok().body(hotelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hotels/:id} : get the "id" hotel.
     *
     * @param id the id of the hotelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hotelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable("id") Long id) {
        log.debug("REST request to get Hotel : {}", id);
        Optional<HotelDTO> hotelDTO = hotelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hotelDTO);
    }

    /**
     * {@code DELETE  /hotels/:id} : delete the "id" hotel.
     *
     * @param id the id of the hotelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("id") Long id) {
        log.debug("REST request to delete Hotel : {}", id);
        hotelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
