package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.RoomPrice;
import com.travel.repository.RoomPriceRepository;
import com.travel.service.criteria.RoomPriceCriteria;
import com.travel.service.dto.RoomPriceDTO;
import com.travel.service.mapper.RoomPriceMapper;
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
 * Service for executing complex queries for {@link RoomPrice} entities in the database.
 * The main input is a {@link RoomPriceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RoomPriceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoomPriceQueryService extends QueryService<RoomPrice> {

    private static final Logger log = LoggerFactory.getLogger(RoomPriceQueryService.class);

    private final RoomPriceRepository roomPriceRepository;

    private final RoomPriceMapper roomPriceMapper;

    public RoomPriceQueryService(RoomPriceRepository roomPriceRepository, RoomPriceMapper roomPriceMapper) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomPriceMapper = roomPriceMapper;
    }

    /**
     * Return a {@link Page} of {@link RoomPriceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoomPriceDTO> findByCriteria(RoomPriceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RoomPrice> specification = createSpecification(criteria);
        return roomPriceRepository.findAll(specification, page).map(roomPriceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoomPriceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RoomPrice> specification = createSpecification(criteria);
        return roomPriceRepository.count(specification);
    }

    /**
     * Function to convert {@link RoomPriceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RoomPrice> createSpecification(RoomPriceCriteria criteria) {
        Specification<RoomPrice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RoomPrice_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), RoomPrice_.price));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), RoomPrice_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), RoomPrice_.toDate));
            }
            if (criteria.getRoomId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoomId(), root -> root.join(RoomPrice_.room, JoinType.LEFT).get(Room_.id))
                );
            }
        }
        return specification;
    }
}
