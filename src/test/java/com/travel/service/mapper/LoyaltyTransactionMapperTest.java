package com.travel.service.mapper;

import static com.travel.domain.LoyaltyTransactionAsserts.*;
import static com.travel.domain.LoyaltyTransactionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoyaltyTransactionMapperTest {

    private LoyaltyTransactionMapper loyaltyTransactionMapper;

    @BeforeEach
    void setUp() {
        loyaltyTransactionMapper = new LoyaltyTransactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLoyaltyTransactionSample1();
        var actual = loyaltyTransactionMapper.toEntity(loyaltyTransactionMapper.toDto(expected));
        assertLoyaltyTransactionAllPropertiesEquals(expected, actual);
    }
}
