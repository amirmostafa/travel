package com.travel.service.mapper;

import com.travel.domain.AgencyService;
import com.travel.service.dto.AgencyServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgencyService} and its DTO {@link AgencyServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgencyServiceMapper extends EntityMapper<AgencyServiceDTO, AgencyService> {}
