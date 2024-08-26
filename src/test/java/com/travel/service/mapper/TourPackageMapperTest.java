package com.travel.service.mapper;

import static com.travel.domain.TourPackageAsserts.*;
import static com.travel.domain.TourPackageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TourPackageMapperTest {

    private TourPackageMapper tourPackageMapper;

    @BeforeEach
    void setUp() {
        tourPackageMapper = new TourPackageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTourPackageSample1();
        var actual = tourPackageMapper.toEntity(tourPackageMapper.toDto(expected));
        assertTourPackageAllPropertiesEquals(expected, actual);
    }
}
