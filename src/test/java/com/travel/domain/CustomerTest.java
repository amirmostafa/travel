package com.travel.domain;

import static com.travel.domain.BookingTestSamples.*;
import static com.travel.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void bookingTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        customer.addBooking(bookingBack);
        assertThat(customer.getBookings()).containsOnly(bookingBack);
        assertThat(bookingBack.getCustomer()).isEqualTo(customer);

        customer.removeBooking(bookingBack);
        assertThat(customer.getBookings()).doesNotContain(bookingBack);
        assertThat(bookingBack.getCustomer()).isNull();

        customer.bookings(new HashSet<>(Set.of(bookingBack)));
        assertThat(customer.getBookings()).containsOnly(bookingBack);
        assertThat(bookingBack.getCustomer()).isEqualTo(customer);

        customer.setBookings(new HashSet<>());
        assertThat(customer.getBookings()).doesNotContain(bookingBack);
        assertThat(bookingBack.getCustomer()).isNull();
    }
}
