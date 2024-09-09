package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.AgencyService;
import com.travel.repository.AgencyServiceRepository;
import com.travel.service.criteria.AgencyServiceCriteria;
import com.travel.service.dto.AgencyServiceDTO;
import com.travel.service.mapper.AgencyServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AgencyService} entities in the database.
 * The main input is a {@link AgencyServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AgencyServiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgencyServiceQueryService extends QueryService<AgencyService> {

    private static final Logger log = LoggerFactory.getLogger(AgencyServiceQueryService.class);

    private final AgencyServiceRepository agencyServiceRepository;

    private final AgencyServiceMapper agencyServiceMapper;

    public AgencyServiceQueryService(AgencyServiceRepository agencyServiceRepository, AgencyServiceMapper agencyServiceMapper) {
        this.agencyServiceRepository = agencyServiceRepository;
        this.agencyServiceMapper = agencyServiceMapper;
    }

    /**
     * Return a {@link Page} of {@link AgencyServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgencyServiceDTO> findByCriteria(AgencyServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AgencyService> specification = createSpecification(criteria);
        return agencyServiceRepository.findAll(specification, page).map(agencyServiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgencyServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AgencyService> specification = createSpecification(criteria);
        return agencyServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link AgencyServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AgencyService> createSpecification(AgencyServiceCriteria criteria) {
        Specification<AgencyService> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AgencyService_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AgencyService_.title));
            }
            if (criteria.getIcon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIcon(), AgencyService_.icon));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), AgencyService_.content));
            }
        }
        return specification;
    }
}
