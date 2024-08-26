package com.travel.service.mapper;

import static com.travel.domain.HotelAsserts.*;
import static com.travel.domain.HotelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HotelMapperTest {

    private HotelMapper hotelMapper;

    @BeforeEach
    void setUp() {
        hotelMapper = new HotelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHotelSample1();
        var actual = hotelMapper.toEntity(hotelMapper.toDto(expected));
        assertHotelAllPropertiesEquals(expected, actual);
    }
}
