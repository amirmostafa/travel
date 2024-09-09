package com.travel.web.rest;

import com.travel.repository.AgencyServiceRepository;
import com.travel.service.AgencyServiceQueryService;
import com.travel.service.AgencyServiceService;
import com.travel.service.criteria.AgencyServiceCriteria;
import com.travel.service.dto.AgencyServiceDTO;
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
 * REST controller for managing {@link com.travel.domain.AgencyService}.
 */
@RestController
@RequestMapping("/api/agency-services")
public class AgencyServiceResource {

    private static final Logger log = LoggerFactory.getLogger(AgencyServiceResource.class);

    private static final String ENTITY_NAME = "agencyService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgencyServiceService agencyServiceService;

    private final AgencyServiceRepository agencyServiceRepository;

    private final AgencyServiceQueryService agencyServiceQueryService;

    public AgencyServiceResource(
        AgencyServiceService agencyServiceService,
        AgencyServiceRepository agencyServiceRepository,
        AgencyServiceQueryService agencyServiceQueryService
    ) {
        this.agencyServiceService = agencyServiceService;
        this.agencyServiceRepository = agencyServiceRepository;
        this.agencyServiceQueryService = agencyServiceQueryService;
    }

    /**
     * {@code POST  /agency-services} : Create a new agencyService.
     *
     * @param agencyServiceDTO the agencyServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agencyServiceDTO, or with status {@code 400 (Bad Request)} if the agencyService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgencyServiceDTO> createAgencyService(@Valid @RequestBody AgencyServiceDTO agencyServiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save AgencyService : {}", agencyServiceDTO);
        if (agencyServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new agencyService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agencyServiceDTO = agencyServiceService.save(agencyServiceDTO);
        return ResponseEntity.created(new URI("/api/agency-services/" + agencyServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agencyServiceDTO.getId().toString()))
            .body(agencyServiceDTO);
    }

    /**
     * {@code PUT  /agency-services/:id} : Updates an existing agencyService.
     *
     * @param id the id of the agencyServiceDTO to save.
     * @param agencyServiceDTO the agencyServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyServiceDTO,
     * or with status {@code 400 (Bad Request)} if the agencyServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agencyServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgencyServiceDTO> updateAgencyService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgencyServiceDTO agencyServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AgencyService : {}, {}", id, agencyServiceDTO);
        if (agencyServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agencyServiceDTO = agencyServiceService.update(agencyServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyServiceDTO.getId().toString()))
            .body(agencyServiceDTO);
    }

    /**
     * {@code PATCH  /agency-services/:id} : Partial updates given fields of an existing agencyService, field will ignore if it is null
     *
     * @param id the id of the agencyServiceDTO to save.
     * @param agencyServiceDTO the agencyServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyServiceDTO,
     * or with status {@code 400 (Bad Request)} if the agencyServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agencyServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agencyServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgencyServiceDTO> partialUpdateAgencyService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgencyServiceDTO agencyServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgencyService partially : {}, {}", id, agencyServiceDTO);
        if (agencyServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgencyServiceDTO> result = agencyServiceService.partialUpdate(agencyServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agency-services} : get all the agencyServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencyServices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgencyServiceDTO>> getAllAgencyServices(
        AgencyServiceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AgencyServices by criteria: {}", criteria);

        Page<AgencyServiceDTO> page = agencyServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agency-services/count} : count all the agencyServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgencyServices(AgencyServiceCriteria criteria) {
        log.debug("REST request to count AgencyServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(agencyServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agency-services/:id} : get the "id" agencyService.
     *
     * @param id the id of the agencyServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agencyServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgencyServiceDTO> getAgencyService(@PathVariable("id") Long id) {
        log.debug("REST request to get AgencyService : {}", id);
        Optional<AgencyServiceDTO> agencyServiceDTO = agencyServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agencyServiceDTO);
    }

    /**
     * {@code DELETE  /agency-services/:id} : delete the "id" agencyService.
     *
     * @param id the id of the agencyServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgencyService(@PathVariable("id") Long id) {
        log.debug("REST request to delete AgencyService : {}", id);
        agencyServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
