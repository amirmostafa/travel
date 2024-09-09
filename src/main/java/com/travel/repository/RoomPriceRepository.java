package com.travel.repository;

import com.travel.domain.RoomPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoomPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomPriceRepository extends JpaRepository<RoomPrice, Long>, JpaSpecificationExecutor<RoomPrice> {}
