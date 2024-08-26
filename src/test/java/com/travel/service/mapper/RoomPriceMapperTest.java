package com.travel.service.mapper;

import static com.travel.domain.RoomPriceAsserts.*;
import static com.travel.domain.RoomPriceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomPriceMapperTest {

    private RoomPriceMapper roomPriceMapper;

    @BeforeEach
    void setUp() {
        roomPriceMapper = new RoomPriceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRoomPriceSample1();
        var actual = roomPriceMapper.toEntity(roomPriceMapper.toDto(expected));
        assertRoomPriceAllPropertiesEquals(expected, actual);
    }
}
