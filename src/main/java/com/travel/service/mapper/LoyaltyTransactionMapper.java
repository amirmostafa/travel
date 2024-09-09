package com.travel.service.mapper;

import com.travel.domain.Customer;
import com.travel.domain.LoyaltyTransaction;
import com.travel.service.dto.CustomerDTO;
import com.travel.service.dto.LoyaltyTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoyaltyTransaction} and its DTO {@link LoyaltyTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoyaltyTransactionMapper extends EntityMapper<LoyaltyTransactionDTO, LoyaltyTransaction> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    LoyaltyTransactionDTO toDto(LoyaltyTransaction s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
