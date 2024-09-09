package com.travel.service;

import com.travel.domain.*; // for static metamodels
import com.travel.domain.Room;
import com.travel.repository.RoomRepository;
import com.travel.service.criteria.RoomCriteria;
import com.travel.service.dto.RoomDTO;
import com.travel.service.mapper.RoomMapper;
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
 * Service for executing complex queries for {@link Room} entities in the database.
 * The main input is a {@link RoomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RoomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoomQueryService extends QueryService<Room> {

    private static final Logger log = LoggerFactory.getLogger(RoomQueryService.class);

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    public RoomQueryService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    /**
     * Return a {@link Page} of {@link RoomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> findByCriteria(RoomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Room> specification = createSpecification(criteria);
        return roomRepository.findAll(specification, page).map(roomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Room> specification = createSpecification(criteria);
        return roomRepository.count(specification);
    }

    /**
     * Function to convert {@link RoomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Room> createSpecification(RoomCriteria criteria) {
        Specification<Room> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Room_.id));
            }
            if (criteria.getRoomNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoomNumber(), Room_.roomNumber));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Room_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Room_.description));
            }
            if (criteria.getDiscountPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountPercentage(), Room_.discountPercentage));
            }
            if (criteria.getRoomPriceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoomPriceId(), root -> root.join(Room_.roomPrices, JoinType.LEFT).get(RoomPrice_.id))
                );
            }
            if (criteria.getImagesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImagesId(), root -> root.join(Room_.images, JoinType.LEFT).get(Image_.id))
                );
            }
            if (criteria.getHotelId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHotelId(), root -> root.join(Room_.hotel, JoinType.LEFT).get(Hotel_.id))
                );
            }
        }
        return specification;
    }
}
