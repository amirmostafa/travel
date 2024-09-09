package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.LoyaltyProgram;
import com.travel.repository.LoyaltyProgramRepository;
import com.travel.service.criteria.LoyaltyProgramCriteria;
import com.travel.service.dto.LoyaltyProgramDTO;
import com.travel.service.mapper.LoyaltyProgramMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LoyaltyProgram} entities in the database.
 * The main input is a {@link LoyaltyProgramCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LoyaltyProgramDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoyaltyProgramQueryService extends QueryService<LoyaltyProgram> {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyProgramQueryService.class);

    private final LoyaltyProgramRepository loyaltyProgramRepository;

    private final LoyaltyProgramMapper loyaltyProgramMapper;

    public LoyaltyProgramQueryService(LoyaltyProgramRepository loyaltyProgramRepository, LoyaltyProgramMapper loyaltyProgramMapper) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
        this.loyaltyProgramMapper = loyaltyProgramMapper;
    }

    /**
     * Return a {@link Page} of {@link LoyaltyProgramDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyProgramDTO> findByCriteria(LoyaltyProgramCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoyaltyProgram> specification = createSpecification(criteria);
        return loyaltyProgramRepository.findAll(specification, page).map(loyaltyProgramMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoyaltyProgramCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoyaltyProgram> specification = createSpecification(criteria);
        return loyaltyProgramRepository.count(specification);
    }

    /**
     * Function to convert {@link LoyaltyProgramCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoyaltyProgram> createSpecification(LoyaltyProgramCriteria criteria) {
        Specification<LoyaltyProgram> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoyaltyProgram_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), LoyaltyProgram_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LoyaltyProgram_.description));
            }
            if (criteria.getPointsPerDollar() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointsPerDollar(), LoyaltyProgram_.pointsPerDollar));
            }
            if (criteria.getRewardThreshold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRewardThreshold(), LoyaltyProgram_.rewardThreshold));
            }
        }
        return specification;
    }
}
