package com.travel.service.mapper;

import com.travel.domain.Booking;
import com.travel.domain.Customer;
import com.travel.service.dto.BookingDTO;
import com.travel.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    BookingDTO toDto(Booking s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
