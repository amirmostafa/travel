package com.travel.service;

import com.travel.domain.Agency;
import com.travel.repository.AgencyRepository;
import com.travel.service.dto.AgencyDTO;
import com.travel.service.mapper.AgencyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.Agency}.
 */
@Service
@Transactional
public class AgencyService {

    private static final Logger log = LoggerFactory.getLogger(AgencyService.class);

    private final AgencyRepository agencyRepository;

    private final AgencyMapper agencyMapper;

    public AgencyService(AgencyRepository agencyRepository, AgencyMapper agencyMapper) {
        this.agencyRepository = agencyRepository;
        this.agencyMapper = agencyMapper;
    }

    /**
     * Save a agency.
     *
     * @param agencyDTO the entity to save.
     * @return the persisted entity.
     */
    public AgencyDTO save(AgencyDTO agencyDTO) {
        log.debug("Request to save Agency : {}", agencyDTO);
        Agency agency = agencyMapper.toEntity(agencyDTO);
        agency = agencyRepository.save(agency);
        return agencyMapper.toDto(agency);
    }

    /**
     * Update a agency.
     *
     * @param agencyDTO the entity to save.
     * @return the persisted entity.
     */
    public AgencyDTO update(AgencyDTO agencyDTO) {
        log.debug("Request to update Agency : {}", agencyDTO);
        Agency agency = agencyMapper.toEntity(agencyDTO);
        agency = agencyRepository.save(agency);
        return agencyMapper.toDto(agency);
    }

    /**
     * Partially update a agency.
     *
     * @param agencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgencyDTO> partialUpdate(AgencyDTO agencyDTO) {
        log.debug("Request to partially update Agency : {}", agencyDTO);

        return agencyRepository
            .findById(agencyDTO.getId())
            .map(existingAgency -> {
                agencyMapper.partialUpdate(existingAgency, agencyDTO);

                return existingAgency;
            })
            .map(agencyRepository::save)
            .map(agencyMapper::toDto);
    }

    /**
     * Get one agency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgencyDTO> findOne(Long id) {
        log.debug("Request to get Agency : {}", id);
        return agencyRepository.findById(id).map(agencyMapper::toDto);
    }

    /**
     * Delete the agency by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Agency : {}", id);
        agencyRepository.deleteById(id);
    }
}
