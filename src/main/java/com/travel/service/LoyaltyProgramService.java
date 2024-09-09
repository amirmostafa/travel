package com.travel.service;

import com.travel.domain.LoyaltyProgram;
import com.travel.repository.LoyaltyProgramRepository;
import com.travel.service.dto.LoyaltyProgramDTO;
import com.travel.service.mapper.LoyaltyProgramMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.LoyaltyProgram}.
 */
@Service
@Transactional
public class LoyaltyProgramService {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyProgramService.class);

    private final LoyaltyProgramRepository loyaltyProgramRepository;

    private final LoyaltyProgramMapper loyaltyProgramMapper;

    public LoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository, LoyaltyProgramMapper loyaltyProgramMapper) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
        this.loyaltyProgramMapper = loyaltyProgramMapper;
    }

    /**
     * Save a loyaltyProgram.
     *
     * @param loyaltyProgramDTO the entity to save.
     * @return the persisted entity.
     */
    public LoyaltyProgramDTO save(LoyaltyProgramDTO loyaltyProgramDTO) {
        log.debug("Request to save LoyaltyProgram : {}", loyaltyProgramDTO);
        LoyaltyProgram loyaltyProgram = loyaltyProgramMapper.toEntity(loyaltyProgramDTO);
        loyaltyProgram = loyaltyProgramRepository.save(loyaltyProgram);
        return loyaltyProgramMapper.toDto(loyaltyProgram);
    }

    /**
     * Update a loyaltyProgram.
     *
     * @param loyaltyProgramDTO the entity to save.
     * @return the persisted entity.
     */
    public LoyaltyProgramDTO update(LoyaltyProgramDTO loyaltyProgramDTO) {
        log.debug("Request to update LoyaltyProgram : {}", loyaltyProgramDTO);
        LoyaltyProgram loyaltyProgram = loyaltyProgramMapper.toEntity(loyaltyProgramDTO);
        loyaltyProgram = loyaltyProgramRepository.save(loyaltyProgram);
        return loyaltyProgramMapper.toDto(loyaltyProgram);
    }

    /**
     * Partially update a loyaltyProgram.
     *
     * @param loyaltyProgramDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LoyaltyProgramDTO> partialUpdate(LoyaltyProgramDTO loyaltyProgramDTO) {
        log.debug("Request to partially update LoyaltyProgram : {}", loyaltyProgramDTO);

        return loyaltyProgramRepository
            .findById(loyaltyProgramDTO.getId())
            .map(existingLoyaltyProgram -> {
                loyaltyProgramMapper.partialUpdate(existingLoyaltyProgram, loyaltyProgramDTO);

                return existingLoyaltyProgram;
            })
            .map(loyaltyProgramRepository::save)
            .map(loyaltyProgramMapper::toDto);
    }

    /**
     * Get one loyaltyProgram by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LoyaltyProgramDTO> findOne(Long id) {
        log.debug("Request to get LoyaltyProgram : {}", id);
        return loyaltyProgramRepository.findById(id).map(loyaltyProgramMapper::toDto);
    }

    /**
     * Delete the loyaltyProgram by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LoyaltyProgram : {}", id);
        loyaltyProgramRepository.deleteById(id);
    }
}
