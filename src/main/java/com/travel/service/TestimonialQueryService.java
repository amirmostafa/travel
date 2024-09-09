package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.Testimonial;
import com.travel.repository.TestimonialRepository;
import com.travel.service.criteria.TestimonialCriteria;
import com.travel.service.dto.TestimonialDTO;
import com.travel.service.mapper.TestimonialMapper;
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
 * Service for executing complex queries for {@link Testimonial} entities in the database.
 * The main input is a {@link TestimonialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TestimonialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestimonialQueryService extends QueryService<Testimonial> {

    private static final Logger log = LoggerFactory.getLogger(TestimonialQueryService.class);

    private final TestimonialRepository testimonialRepository;

    private final TestimonialMapper testimonialMapper;

    public TestimonialQueryService(TestimonialRepository testimonialRepository, TestimonialMapper testimonialMapper) {
        this.testimonialRepository = testimonialRepository;
        this.testimonialMapper = testimonialMapper;
    }

    /**
     * Return a {@link Page} of {@link TestimonialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestimonialDTO> findByCriteria(TestimonialCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Testimonial> specification = createSpecification(criteria);
        return testimonialRepository.findAll(specification, page).map(testimonialMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestimonialCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Testimonial> specification = createSpecification(criteria);
        return testimonialRepository.count(specification);
    }

    /**
     * Function to convert {@link TestimonialCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Testimonial> createSpecification(TestimonialCriteria criteria) {
        Specification<Testimonial> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Testimonial_.id));
            }
            if (criteria.getAuthorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthorName(), Testimonial_.authorName));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Testimonial_.content));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Testimonial_.rating));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Testimonial_.date));
            }
            if (criteria.getHotelId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHotelId(), root -> root.join(Testimonial_.hotels, JoinType.LEFT).get(Hotel_.id))
                );
            }
        }
        return specification;
    }
}
