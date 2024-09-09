package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.Hotel;
import com.travel.repository.HotelRepository;
import com.travel.service.criteria.HotelCriteria;
import com.travel.service.dto.HotelDTO;
import com.travel.service.mapper.HotelMapper;
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
 * Service for executing complex queries for {@link Hotel} entities in the database.
 * The main input is a {@link HotelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HotelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HotelQueryService extends QueryService<Hotel> {

    private static final Logger log = LoggerFactory.getLogger(HotelQueryService.class);

    private final HotelRepository hotelRepository;

    private final HotelMapper hotelMapper;

    public HotelQueryService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    /**
     * Return a {@link Page} of {@link HotelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HotelDTO> findByCriteria(HotelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Hotel> specification = createSpecification(criteria);
        return hotelRepository.findAll(specification, page).map(hotelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HotelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Hotel> specification = createSpecification(criteria);
        return hotelRepository.count(specification);
    }

    /**
     * Function to convert {@link HotelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Hotel> createSpecification(HotelCriteria criteria) {
        Specification<Hotel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Hotel_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Hotel_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Hotel_.address));
            }
            if (criteria.getStarRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStarRating(), Hotel_.starRating));
            }
            if (criteria.getContactNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactNumber(), Hotel_.contactNumber));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Hotel_.email));
            }
            if (criteria.getCountryCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryCode(), Hotel_.countryCode));
            }
            if (criteria.getCityCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCityCode(), Hotel_.cityCode));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), Hotel_.imageUrl));
            }
            if (criteria.getRoomId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoomId(), root -> root.join(Hotel_.rooms, JoinType.LEFT).get(Room_.id))
                );
            }
            if (criteria.getTestimonialId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getTestimonialId(),
                        root -> root.join(Hotel_.testimonial, JoinType.LEFT).get(Testimonial_.id)
                    )
                );
            }
        }
        return specification;
    }
}
