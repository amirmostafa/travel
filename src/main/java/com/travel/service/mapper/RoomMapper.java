package com.travel.service.mapper;

import com.travel.domain.Hotel;
import com.travel.domain.Room;
import com.travel.service.dto.HotelDTO;
import com.travel.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
    @Mapping(target = "hotel", source = "hotel", qualifiedByName = "hotelId")
    RoomDTO toDto(Room s);

    @Named("hotelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HotelDTO toDtoHotelId(Hotel hotel);
}
