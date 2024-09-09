package com.travel.service.mapper;

import static com.travel.domain.AboutUsAsserts.*;
import static com.travel.domain.AboutUsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AboutUsMapperTest {

    private AboutUsMapper aboutUsMapper;

    @BeforeEach
    void setUp() {
        aboutUsMapper = new AboutUsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAboutUsSample1();
        var actual = aboutUsMapper.toEntity(aboutUsMapper.toDto(expected));
        assertAboutUsAllPropertiesEquals(expected, actual);
    }
}
