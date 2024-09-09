package com.travel.service.mapper;

import com.travel.domain.LoyaltyProgram;
import com.travel.service.dto.LoyaltyProgramDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoyaltyProgram} and its DTO {@link LoyaltyProgramDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoyaltyProgramMapper extends EntityMapper<LoyaltyProgramDTO, LoyaltyProgram> {}
