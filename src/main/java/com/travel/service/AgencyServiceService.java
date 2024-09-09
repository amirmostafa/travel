package com.travel.service;

import com.travel.domain.AgencyService;
import com.travel.repository.AgencyServiceRepository;
import com.travel.service.dto.AgencyServiceDTO;
import com.travel.service.mapper.AgencyServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.AgencyService}.
 */
@Service
@Transactional
public class AgencyServiceService {

    private static final Logger log = LoggerFactory.getLogger(AgencyServiceService.class);

    private final AgencyServiceRepository agencyServiceRepository;

    private final AgencyServiceMapper agencyServiceMapper;

    public AgencyServiceService(AgencyServiceRepository agencyServiceRepository, AgencyServiceMapper agencyServiceMapper) {
        this.agencyServiceRepository = agencyServiceRepository;
        this.agencyServiceMapper = agencyServiceMapper;
    }

    /**
     * Save a agencyService.
     *
     * @param agencyServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public AgencyServiceDTO save(AgencyServiceDTO agencyServiceDTO) {
        log.debug("Request to save AgencyService : {}", agencyServiceDTO);
        AgencyService agencyService = agencyServiceMapper.toEntity(agencyServiceDTO);
        agencyService = agencyServiceRepository.save(agencyService);
        return agencyServiceMapper.toDto(agencyService);
    }

    /**
     * Update a agencyService.
     *
     * @param agencyServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public AgencyServiceDTO update(AgencyServiceDTO agencyServiceDTO) {
        log.debug("Request to update AgencyService : {}", agencyServiceDTO);
        AgencyService agencyService = agencyServiceMapper.toEntity(agencyServiceDTO);
        agencyService = agencyServiceRepository.save(agencyService);
        return agencyServiceMapper.toDto(agencyService);
    }

    /**
     * Partially update a agencyService.
     *
     * @param agencyServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgencyServiceDTO> partialUpdate(AgencyServiceDTO agencyServiceDTO) {
        log.debug("Request to partially update AgencyService : {}", agencyServiceDTO);

        return agencyServiceRepository
            .findById(agencyServiceDTO.getId())
            .map(existingAgencyService -> {
                agencyServiceMapper.partialUpdate(existingAgencyService, agencyServiceDTO);

                return existingAgencyService;
            })
            .map(agencyServiceRepository::save)
            .map(agencyServiceMapper::toDto);
    }

    /**
     * Get one agencyService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgencyServiceDTO> findOne(Long id) {
        log.debug("Request to get AgencyService : {}", id);
        return agencyServiceRepository.findById(id).map(agencyServiceMapper::toDto);
    }

    /**
     * Delete the agencyService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgencyService : {}", id);
        agencyServiceRepository.deleteById(id);
    }
}
