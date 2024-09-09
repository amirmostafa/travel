package com.travel.service.mapper;

import static com.travel.domain.LoyaltyProgramAsserts.*;
import static com.travel.domain.LoyaltyProgramTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoyaltyProgramMapperTest {

    private LoyaltyProgramMapper loyaltyProgramMapper;

    @BeforeEach
    void setUp() {
        loyaltyProgramMapper = new LoyaltyProgramMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLoyaltyProgramSample1();
        var actual = loyaltyProgramMapper.toEntity(loyaltyProgramMapper.toDto(expected));
        assertLoyaltyProgramAllPropertiesEquals(expected, actual);
    }
}
