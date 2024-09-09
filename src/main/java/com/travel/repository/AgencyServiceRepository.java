package com.travel.repository;

import com.travel.domain.AgencyService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgencyService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgencyServiceRepository extends JpaRepository<AgencyService, Long>, JpaSpecificationExecutor<AgencyService> {}
