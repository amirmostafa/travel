package com.travel.repository;

import com.travel.domain.TourPackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TourPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long>, JpaSpecificationExecutor<TourPackage> {}
