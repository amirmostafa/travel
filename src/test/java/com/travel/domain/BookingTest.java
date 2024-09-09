package com.travel.domain;

import static com.travel.domain.BookingTestSamples.*;
import static com.travel.domain.CustomerTestSamples.*;
import static com.travel.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void paymentTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        booking.addPayment(paymentBack);
        assertThat(booking.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBooking()).isEqualTo(booking);

        booking.removePayment(paymentBack);
        assertThat(booking.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBooking()).isNull();

        booking.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(booking.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBooking()).isEqualTo(booking);

        booking.setPayments(new HashSet<>());
        assertThat(booking.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBooking()).isNull();
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
