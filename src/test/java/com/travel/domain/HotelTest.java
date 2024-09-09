package com.travel.domain;

import static com.travel.domain.HotelTestSamples.*;
import static com.travel.domain.RoomTestSamples.*;
import static com.travel.domain.TestimonialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HotelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hotel.class);
        Hotel hotel1 = getHotelSample1();
        Hotel hotel2 = new Hotel();
        assertThat(hotel1).isNotEqualTo(hotel2);

        hotel2.setId(hotel1.getId());
        assertThat(hotel1).isEqualTo(hotel2);

        hotel2 = getHotelSample2();
        assertThat(hotel1).isNotEqualTo(hotel2);
    }

    @Test
    void roomTest() {
        Hotel hotel = getHotelRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        hotel.addRoom(roomBack);
        assertThat(hotel.getRooms()).containsOnly(roomBack);
        assertThat(roomBack.getHotel()).isEqualTo(hotel);

        hotel.removeRoom(roomBack);
        assertThat(hotel.getRooms()).doesNotContain(roomBack);
        assertThat(roomBack.getHotel()).isNull();

        hotel.rooms(new HashSet<>(Set.of(roomBack)));
        assertThat(hotel.getRooms()).containsOnly(roomBack);
        assertThat(roomBack.getHotel()).isEqualTo(hotel);

        hotel.setRooms(new HashSet<>());
        assertThat(hotel.getRooms()).doesNotContain(roomBack);
        assertThat(roomBack.getHotel()).isNull();
    }

    @Test
    void testimonialTest() {
        Hotel hotel = getHotelRandomSampleGenerator();
        Testimonial testimonialBack = getTestimonialRandomSampleGenerator();

        hotel.setTestimonial(testimonialBack);
        assertThat(hotel.getTestimonial()).isEqualTo(testimonialBack);

        hotel.testimonial(null);
        assertThat(hotel.getTestimonial()).isNull();
    }
}
