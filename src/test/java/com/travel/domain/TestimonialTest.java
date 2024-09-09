package com.travel.domain;

import static com.travel.domain.HotelTestSamples.*;
import static com.travel.domain.TestimonialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestimonialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Testimonial.class);
        Testimonial testimonial1 = getTestimonialSample1();
        Testimonial testimonial2 = new Testimonial();
        assertThat(testimonial1).isNotEqualTo(testimonial2);

        testimonial2.setId(testimonial1.getId());
        assertThat(testimonial1).isEqualTo(testimonial2);

        testimonial2 = getTestimonialSample2();
        assertThat(testimonial1).isNotEqualTo(testimonial2);
    }

    @Test
    void hotelTest() {
        Testimonial testimonial = getTestimonialRandomSampleGenerator();
        Hotel hotelBack = getHotelRandomSampleGenerator();

        testimonial.addHotel(hotelBack);
        assertThat(testimonial.getHotels()).containsOnly(hotelBack);
        assertThat(hotelBack.getTestimonial()).isEqualTo(testimonial);

        testimonial.removeHotel(hotelBack);
        assertThat(testimonial.getHotels()).doesNotContain(hotelBack);
        assertThat(hotelBack.getTestimonial()).isNull();

        testimonial.hotels(new HashSet<>(Set.of(hotelBack)));
        assertThat(testimonial.getHotels()).containsOnly(hotelBack);
        assertThat(hotelBack.getTestimonial()).isEqualTo(testimonial);

        testimonial.setHotels(new HashSet<>());
        assertThat(testimonial.getHotels()).doesNotContain(hotelBack);
        assertThat(hotelBack.getTestimonial()).isNull();
    }
}
