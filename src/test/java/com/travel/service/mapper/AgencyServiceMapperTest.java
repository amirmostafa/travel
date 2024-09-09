package com.travel.service.mapper;

import static com.travel.domain.AgencyServiceAsserts.*;
import static com.travel.domain.AgencyServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgencyServiceMapperTest {

    private AgencyServiceMapper agencyServiceMapper;

    @BeforeEach
    void setUp() {
        agencyServiceMapper = new AgencyServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAgencyServiceSample1();
        var actual = agencyServiceMapper.toEntity(agencyServiceMapper.toDto(expected));
        assertAgencyServiceAllPropertiesEquals(expected, actual);
    }
}
