package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.LoyaltyTransaction;
import com.travel.repository.LoyaltyTransactionRepository;
import com.travel.service.criteria.LoyaltyTransactionCriteria;
import com.travel.service.dto.LoyaltyTransactionDTO;
import com.travel.service.mapper.LoyaltyTransactionMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LoyaltyTransaction} entities in the database.
 * The main input is a {@link LoyaltyTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LoyaltyTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoyaltyTransactionQueryService extends QueryService<LoyaltyTransaction> {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyTransactionQueryService.class);

    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    private final LoyaltyTransactionMapper loyaltyTransactionMapper;

    public LoyaltyTransactionQueryService(
        LoyaltyTransactionRepository loyaltyTransactionRepository,
        LoyaltyTransactionMapper loyaltyTransactionMapper
    ) {
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
        this.loyaltyTransactionMapper = loyaltyTransactionMapper;
    }

    /**
     * Return a {@link Page} of {@link LoyaltyTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyTransactionDTO> findByCriteria(LoyaltyTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoyaltyTransaction> specification = createSpecification(criteria);
        return loyaltyTransactionRepository.findAll(specification, page).map(loyaltyTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoyaltyTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoyaltyTransaction> specification = createSpecification(criteria);
        return loyaltyTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link LoyaltyTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoyaltyTransaction> createSpecification(LoyaltyTransactionCriteria criteria) {
        Specification<LoyaltyTransaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoyaltyTransaction_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), LoyaltyTransaction_.date));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), LoyaltyTransaction_.points));
            }
            if (criteria.getTransactionType() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionType(), LoyaltyTransaction_.transactionType));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LoyaltyTransaction_.description));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCustomerId(),
                        root -> root.join(LoyaltyTransaction_.customer, JoinType.LEFT).get(Customer_.id)
                    )
                );
            }
        }
        return specification;
    }
}
