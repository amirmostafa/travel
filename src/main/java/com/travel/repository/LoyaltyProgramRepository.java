package com.travel.repository;

import com.travel.domain.LoyaltyProgram;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoyaltyProgram entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Long>, JpaSpecificationExecutor<LoyaltyProgram> {}
