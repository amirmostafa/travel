package com.travel.service.mapper;

import com.travel.domain.Room;
import com.travel.domain.RoomPrice;
import com.travel.service.dto.RoomDTO;
import com.travel.service.dto.RoomPriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoomPrice} and its DTO {@link RoomPriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomPriceMapper extends EntityMapper<RoomPriceDTO, RoomPrice> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    RoomPriceDTO toDto(RoomPrice s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);
}
