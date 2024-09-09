package com.travel.service.mapper;

import com.travel.domain.Hotel;
import com.travel.domain.Testimonial;
import com.travel.service.dto.HotelDTO;
import com.travel.service.dto.TestimonialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hotel} and its DTO {@link HotelDTO}.
 */
@Mapper(componentModel = "spring")
public interface HotelMapper extends EntityMapper<HotelDTO, Hotel> {
    @Mapping(target = "testimonial", source = "testimonial", qualifiedByName = "testimonialId")
    HotelDTO toDto(Hotel s);

    @Named("testimonialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TestimonialDTO toDtoTestimonialId(Testimonial testimonial);
}
