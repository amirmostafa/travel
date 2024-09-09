package com.travel.service.mapper;

import com.travel.domain.AboutUs;
import com.travel.service.dto.AboutUsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AboutUs} and its DTO {@link AboutUsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AboutUsMapper extends EntityMapper<AboutUsDTO, AboutUs> {}
