package com.travel.service.mapper;

import com.travel.domain.Booking;
import com.travel.domain.Customer;
import com.travel.domain.Room;
import com.travel.domain.TourPackage;
import com.travel.service.dto.BookingDTO;
import com.travel.service.dto.CustomerDTO;
import com.travel.service.dto.RoomDTO;
import com.travel.service.dto.TourPackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    @Mapping(target = "tourPackage", source = "tourPackage", qualifiedByName = "tourPackageId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    BookingDTO toDto(Booking s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);

    @Named("tourPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TourPackageDTO toDtoTourPackageId(TourPackage tourPackage);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
