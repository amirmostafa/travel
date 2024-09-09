package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.TourPackage;
import com.travel.repository.TourPackageRepository;
import com.travel.service.criteria.TourPackageCriteria;
import com.travel.service.dto.TourPackageDTO;
import com.travel.service.mapper.TourPackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TourPackage} entities in the database.
 * The main input is a {@link TourPackageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TourPackageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TourPackageQueryService extends QueryService<TourPackage> {

    private static final Logger log = LoggerFactory.getLogger(TourPackageQueryService.class);

    private final TourPackageRepository tourPackageRepository;

    private final TourPackageMapper tourPackageMapper;

    public TourPackageQueryService(TourPackageRepository tourPackageRepository, TourPackageMapper tourPackageMapper) {
        this.tourPackageRepository = tourPackageRepository;
        this.tourPackageMapper = tourPackageMapper;
    }

    /**
     * Return a {@link Page} of {@link TourPackageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TourPackageDTO> findByCriteria(TourPackageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TourPackage> specification = createSpecification(criteria);
        return tourPackageRepository.findAll(specification, page).map(tourPackageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TourPackageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TourPackage> specification = createSpecification(criteria);
        return tourPackageRepository.count(specification);
    }

    /**
     * Function to convert {@link TourPackageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TourPackage> createSpecification(TourPackageCriteria criteria) {
        Specification<TourPackage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TourPackage_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TourPackage_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TourPackage_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), TourPackage_.price));
            }
            if (criteria.getDurationDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationDays(), TourPackage_.durationDays));
            }
            if (criteria.getAvailable() != null) {
                specification = specification.and(buildSpecification(criteria.getAvailable(), TourPackage_.available));
            }
        }
        return specification;
    }
}
