package com.travel.service.mapper;

import com.travel.domain.Agency;
import com.travel.domain.TourPackage;
import com.travel.service.dto.AgencyDTO;
import com.travel.service.dto.TourPackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TourPackage} and its DTO {@link TourPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface TourPackageMapper extends EntityMapper<TourPackageDTO, TourPackage> {
    @Mapping(target = "agency", source = "agency", qualifiedByName = "agencyId")
    TourPackageDTO toDto(TourPackage s);

    @Named("agencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgencyDTO toDtoAgencyId(Agency agency);
}
