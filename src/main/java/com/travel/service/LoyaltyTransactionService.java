package com.travel.service;

import com.travel.domain.LoyaltyTransaction;
import com.travel.repository.LoyaltyTransactionRepository;
import com.travel.service.dto.LoyaltyTransactionDTO;
import com.travel.service.mapper.LoyaltyTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.LoyaltyTransaction}.
 */
@Service
@Transactional
public class LoyaltyTransactionService {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyTransactionService.class);

    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    private final LoyaltyTransactionMapper loyaltyTransactionMapper;

    public LoyaltyTransactionService(
        LoyaltyTransactionRepository loyaltyTransactionRepository,
        LoyaltyTransactionMapper loyaltyTransactionMapper
    ) {
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
        this.loyaltyTransactionMapper = loyaltyTransactionMapper;
    }

    /**
     * Save a loyaltyTransaction.
     *
     * @param loyaltyTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public LoyaltyTransactionDTO save(LoyaltyTransactionDTO loyaltyTransactionDTO) {
        log.debug("Request to save LoyaltyTransaction : {}", loyaltyTransactionDTO);
        LoyaltyTransaction loyaltyTransaction = loyaltyTransactionMapper.toEntity(loyaltyTransactionDTO);
        loyaltyTransaction = loyaltyTransactionRepository.save(loyaltyTransaction);
        return loyaltyTransactionMapper.toDto(loyaltyTransaction);
    }

    /**
     * Update a loyaltyTransaction.
     *
     * @param loyaltyTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public LoyaltyTransactionDTO update(LoyaltyTransactionDTO loyaltyTransactionDTO) {
        log.debug("Request to update LoyaltyTransaction : {}", loyaltyTransactionDTO);
        LoyaltyTransaction loyaltyTransaction = loyaltyTransactionMapper.toEntity(loyaltyTransactionDTO);
        loyaltyTransaction = loyaltyTransactionRepository.save(loyaltyTransaction);
        return loyaltyTransactionMapper.toDto(loyaltyTransaction);
    }

    /**
     * Partially update a loyaltyTransaction.
     *
     * @param loyaltyTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LoyaltyTransactionDTO> partialUpdate(LoyaltyTransactionDTO loyaltyTransactionDTO) {
        log.debug("Request to partially update LoyaltyTransaction : {}", loyaltyTransactionDTO);

        return loyaltyTransactionRepository
            .findById(loyaltyTransactionDTO.getId())
            .map(existingLoyaltyTransaction -> {
                loyaltyTransactionMapper.partialUpdate(existingLoyaltyTransaction, loyaltyTransactionDTO);

                return existingLoyaltyTransaction;
            })
            .map(loyaltyTransactionRepository::save)
            .map(loyaltyTransactionMapper::toDto);
    }

    /**
     * Get one loyaltyTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LoyaltyTransactionDTO> findOne(Long id) {
        log.debug("Request to get LoyaltyTransaction : {}", id);
        return loyaltyTransactionRepository.findById(id).map(loyaltyTransactionMapper::toDto);
    }

    /**
     * Delete the loyaltyTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LoyaltyTransaction : {}", id);
        loyaltyTransactionRepository.deleteById(id);
    }
}
