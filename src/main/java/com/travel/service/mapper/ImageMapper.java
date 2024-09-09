package com.travel.service.mapper;

import com.travel.domain.Image;
import com.travel.domain.Room;
import com.travel.service.dto.ImageDTO;
import com.travel.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    ImageDTO toDto(Image s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);
}
