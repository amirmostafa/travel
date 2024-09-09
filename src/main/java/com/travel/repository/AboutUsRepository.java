package com.travel.repository;

import com.travel.domain.AboutUs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AboutUs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutUsRepository extends JpaRepository<AboutUs, Long>, JpaSpecificationExecutor<AboutUs> {}
