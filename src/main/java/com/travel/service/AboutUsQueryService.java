package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.AboutUs;
import com.travel.repository.AboutUsRepository;
import com.travel.service.criteria.AboutUsCriteria;
import com.travel.service.dto.AboutUsDTO;
import com.travel.service.mapper.AboutUsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AboutUs} entities in the database.
 * The main input is a {@link AboutUsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AboutUsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AboutUsQueryService extends QueryService<AboutUs> {

    private static final Logger log = LoggerFactory.getLogger(AboutUsQueryService.class);

    private final AboutUsRepository aboutUsRepository;

    private final AboutUsMapper aboutUsMapper;

    public AboutUsQueryService(AboutUsRepository aboutUsRepository, AboutUsMapper aboutUsMapper) {
        this.aboutUsRepository = aboutUsRepository;
        this.aboutUsMapper = aboutUsMapper;
    }

    /**
     * Return a {@link Page} of {@link AboutUsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AboutUsDTO> findByCriteria(AboutUsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AboutUs> specification = createSpecification(criteria);
        return aboutUsRepository.findAll(specification, page).map(aboutUsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AboutUsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AboutUs> specification = createSpecification(criteria);
        return aboutUsRepository.count(specification);
    }

    /**
     * Function to convert {@link AboutUsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AboutUs> createSpecification(AboutUsCriteria criteria) {
        Specification<AboutUs> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AboutUs_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AboutUs_.description));
            }
            if (criteria.getContactDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactDetails(), AboutUs_.contactDetails));
            }
            if (criteria.getAdditionalInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdditionalInfo(), AboutUs_.additionalInfo));
            }
        }
        return specification;
    }
}
