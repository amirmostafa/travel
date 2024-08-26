package com.travel.service.mapper;

import com.travel.domain.Agency;
import com.travel.service.dto.AgencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agency} and its DTO {@link AgencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgencyMapper extends EntityMapper<AgencyDTO, Agency> {}
