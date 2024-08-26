package com.travel.domain;

import static com.travel.domain.BookingTestSamples.*;
import static com.travel.domain.CustomerTestSamples.*;
import static com.travel.domain.RoomTestSamples.*;
import static com.travel.domain.TourPackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = getBookingSample1();
        Booking booking2 = new Booking();
        assertThat(booking1).isNotEqualTo(booking2);

        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);

        booking2 = getBookingSample2();
        assertThat(booking1).isNotEqualTo(booking2);
    }

    @Test
    void roomTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        booking.setRoom(roomBack);
        assertThat(booking.getRoom()).isEqualTo(roomBack);

        booking.room(null);
        assertThat(booking.getRoom()).isNull();
    }

    @Test
    void tourPackageTest() {
        Booking booking = getBookingRandomSampleGenerator();
        TourPackage tourPackageBack = getTourPackageRandomSampleGenerator();

        booking.setTourPackage(tourPackageBack);
        assertThat(booking.getTourPackage()).isEqualTo(tourPackageBack);

        booking.tourPackage(null);
        assertThat(booking.getTourPackage()).isNull();
    }

    @Test
    void customerTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        booking.setCustomer(customerBack);
        assertThat(booking.getCustomer()).isEqualTo(customerBack);

        booking.customer(null);
        assertThat(booking.getCustomer()).isNull();
    }
}
