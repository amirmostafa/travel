package com.travel.repository;

import com.travel.domain.Testimonial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Testimonial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, Long>, JpaSpecificationExecutor<Testimonial> {}
