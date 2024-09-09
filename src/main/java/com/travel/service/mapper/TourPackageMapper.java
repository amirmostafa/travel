package com.travel.service.mapper;

import com.travel.domain.TourPackage;
import com.travel.service.dto.TourPackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TourPackage} and its DTO {@link TourPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface TourPackageMapper extends EntityMapper<TourPackageDTO, TourPackage> {}
