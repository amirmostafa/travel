package com.travel.repository;

import com.travel.domain.LoyaltyTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoyaltyTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoyaltyTransactionRepository
    extends JpaRepository<LoyaltyTransaction, Long>, JpaSpecificationExecutor<LoyaltyTransaction> {}
