package com.travel.web.rest;

import com.travel.repository.RoomPriceRepository;
import com.travel.service.RoomPriceService;
import com.travel.service.dto.RoomPriceDTO;
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
 * REST controller for managing {@link com.travel.domain.RoomPrice}.
 */
@RestController
@RequestMapping("/api/room-prices")
public class RoomPriceResource {

    private static final Logger log = LoggerFactory.getLogger(RoomPriceResource.class);

    private static final String ENTITY_NAME = "roomPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomPriceService roomPriceService;

    private final RoomPriceRepository roomPriceRepository;

    public RoomPriceResource(RoomPriceService roomPriceService, RoomPriceRepository roomPriceRepository) {
        this.roomPriceService = roomPriceService;
        this.roomPriceRepository = roomPriceRepository;
    }

    /**
     * {@code POST  /room-prices} : Create a new roomPrice.
     *
     * @param roomPriceDTO the roomPriceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomPriceDTO, or with status {@code 400 (Bad Request)} if the roomPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoomPriceDTO> createRoomPrice(@Valid @RequestBody RoomPriceDTO roomPriceDTO) throws URISyntaxException {
        log.debug("REST request to save RoomPrice : {}", roomPriceDTO);
        if (roomPriceDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roomPriceDTO = roomPriceService.save(roomPriceDTO);
        return ResponseEntity.created(new URI("/api/room-prices/" + roomPriceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roomPriceDTO.getId().toString()))
            .body(roomPriceDTO);
    }

    /**
     * {@code PUT  /room-prices/:id} : Updates an existing roomPrice.
     *
     * @param id the id of the roomPriceDTO to save.
     * @param roomPriceDTO the roomPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomPriceDTO,
     * or with status {@code 400 (Bad Request)} if the roomPriceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomPriceDTO> updateRoomPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoomPriceDTO roomPriceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoomPrice : {}, {}", id, roomPriceDTO);
        if (roomPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roomPriceDTO = roomPriceService.update(roomPriceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomPriceDTO.getId().toString()))
            .body(roomPriceDTO);
    }

    /**
     * {@code PATCH  /room-prices/:id} : Partial updates given fields of an existing roomPrice, field will ignore if it is null
     *
     * @param id the id of the roomPriceDTO to save.
     * @param roomPriceDTO the roomPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomPriceDTO,
     * or with status {@code 400 (Bad Request)} if the roomPriceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roomPriceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomPriceDTO> partialUpdateRoomPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoomPriceDTO roomPriceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomPrice partially : {}, {}", id, roomPriceDTO);
        if (roomPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomPriceDTO> result = roomPriceService.partialUpdate(roomPriceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomPriceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /room-prices} : get all the roomPrices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomPrices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RoomPriceDTO>> getAllRoomPrices(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RoomPrices");
        Page<RoomPriceDTO> page = roomPriceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-prices/:id} : get the "id" roomPrice.
     *
     * @param id the id of the roomPriceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomPriceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomPriceDTO> getRoomPrice(@PathVariable("id") Long id) {
        log.debug("REST request to get RoomPrice : {}", id);
        Optional<RoomPriceDTO> roomPriceDTO = roomPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomPriceDTO);
    }

    /**
     * {@code DELETE  /room-prices/:id} : delete the "id" roomPrice.
     *
     * @param id the id of the roomPriceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomPrice(@PathVariable("id") Long id) {
        log.debug("REST request to delete RoomPrice : {}", id);
        roomPriceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
