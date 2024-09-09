package com.travel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestimonialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestimonialDTO.class);
        TestimonialDTO testimonialDTO1 = new TestimonialDTO();
        testimonialDTO1.setId(1L);
        TestimonialDTO testimonialDTO2 = new TestimonialDTO();
        assertThat(testimonialDTO1).isNotEqualTo(testimonialDTO2);
        testimonialDTO2.setId(testimonialDTO1.getId());
        assertThat(testimonialDTO1).isEqualTo(testimonialDTO2);
        testimonialDTO2.setId(2L);
        assertThat(testimonialDTO1).isNotEqualTo(testimonialDTO2);
        testimonialDTO1.setId(null);
        assertThat(testimonialDTO1).isNotEqualTo(testimonialDTO2);
    }
}
