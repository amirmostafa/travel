package com.travel.service.mapper;

import static com.travel.domain.TestimonialAsserts.*;
import static com.travel.domain.TestimonialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestimonialMapperTest {

    private TestimonialMapper testimonialMapper;

    @BeforeEach
    void setUp() {
        testimonialMapper = new TestimonialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTestimonialSample1();
        var actual = testimonialMapper.toEntity(testimonialMapper.toDto(expected));
        assertTestimonialAllPropertiesEquals(expected, actual);
    }
}
