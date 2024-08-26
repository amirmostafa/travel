package com.travel.service.mapper;

import static com.travel.domain.AgencyAsserts.*;
import static com.travel.domain.AgencyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgencyMapperTest {

    private AgencyMapper agencyMapper;

    @BeforeEach
    void setUp() {
        agencyMapper = new AgencyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAgencySample1();
        var actual = agencyMapper.toEntity(agencyMapper.toDto(expected));
        assertAgencyAllPropertiesEquals(expected, actual);
    }
}
