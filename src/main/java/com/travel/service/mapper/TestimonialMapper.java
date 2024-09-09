package com.travel.service.mapper;

import com.travel.domain.Testimonial;
import com.travel.service.dto.TestimonialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Testimonial} and its DTO {@link TestimonialDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestimonialMapper extends EntityMapper<TestimonialDTO, Testimonial> {}
