package com.travel.web.rest;

import com.travel.repository.AgencyRepository;
import com.travel.service.AgencyQueryService;
import com.travel.service.AgencyService;
import com.travel.service.criteria.AgencyCriteria;
import com.travel.service.dto.AgencyDTO;
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
 * REST controller for managing {@link com.travel.domain.Agency}.
 */
@RestController
@RequestMapping("/api/agencies")
public class AgencyResource {

    private static final Logger log = LoggerFactory.getLogger(AgencyResource.class);

    private static final String ENTITY_NAME = "agency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgencyService agencyService;

    private final AgencyRepository agencyRepository;

    private final AgencyQueryService agencyQueryService;

    public AgencyResource(AgencyService agencyService, AgencyRepository agencyRepository, AgencyQueryService agencyQueryService) {
        this.agencyService = agencyService;
        this.agencyRepository = agencyRepository;
        this.agencyQueryService = agencyQueryService;
    }

    /**
     * {@code POST  /agencies} : Create a new agency.
     *
     * @param agencyDTO the agencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agencyDTO, or with status {@code 400 (Bad Request)} if the agency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgencyDTO> createAgency(@Valid @RequestBody AgencyDTO agencyDTO) throws URISyntaxException {
        log.debug("REST request to save Agency : {}", agencyDTO);
        if (agencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new agency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agencyDTO = agencyService.save(agencyDTO);
        return ResponseEntity.created(new URI("/api/agencies/" + agencyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agencyDTO.getId().toString()))
            .body(agencyDTO);
    }

    /**
     * {@code PUT  /agencies/:id} : Updates an existing agency.
     *
     * @param id the id of the agencyDTO to save.
     * @param agencyDTO the agencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyDTO,
     * or with status {@code 400 (Bad Request)} if the agencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgencyDTO> updateAgency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgencyDTO agencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Agency : {}, {}", id, agencyDTO);
        if (agencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agencyDTO = agencyService.update(agencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyDTO.getId().toString()))
            .body(agencyDTO);
    }

    /**
     * {@code PATCH  /agencies/:id} : Partial updates given fields of an existing agency, field will ignore if it is null
     *
     * @param id the id of the agencyDTO to save.
     * @param agencyDTO the agencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyDTO,
     * or with status {@code 400 (Bad Request)} if the agencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgencyDTO> partialUpdateAgency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgencyDTO agencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agency partially : {}, {}", id, agencyDTO);
        if (agencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgencyDTO> result = agencyService.partialUpdate(agencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agencies} : get all the agencies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgencyDTO>> getAllAgencies(
        AgencyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Agencies by criteria: {}", criteria);

        Page<AgencyDTO> page = agencyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agencies/count} : count all the agencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgencies(AgencyCriteria criteria) {
        log.debug("REST request to count Agencies by criteria: {}", criteria);
        return ResponseEntity.ok().body(agencyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agencies/:id} : get the "id" agency.
     *
     * @param id the id of the agencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgencyDTO> getAgency(@PathVariable("id") Long id) {
        log.debug("REST request to get Agency : {}", id);
        Optional<AgencyDTO> agencyDTO = agencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agencyDTO);
    }

    /**
     * {@code DELETE  /agencies/:id} : delete the "id" agency.
     *
     * @param id the id of the agencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable("id") Long id) {
        log.debug("REST request to delete Agency : {}", id);
        agencyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
