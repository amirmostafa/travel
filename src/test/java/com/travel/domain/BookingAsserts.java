package com.travel.domain;

import static com.travel.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookingAllPropertiesEquals(Booking expected, Booking actual) {
        assertBookingAutoGeneratedPropertiesEquals(expected, actual);
        assertBookingAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookingAllUpdatablePropertiesEquals(Booking expected, Booking actual) {
        assertBookingUpdatableFieldsEquals(expected, actual);
        assertBookingUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookingAutoGeneratedPropertiesEquals(Booking expected, Booking actual) {
        assertThat(expected)
            .as("Verify Booking auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookingUpdatableFieldsEquals(Booking expected, Booking actual) {
        assertThat(expected)
            .as("Verify Booking relevant properties")
            .satisfies(e -> assertThat(e.getBookingDate()).as("check bookingDate").isEqualTo(actual.getBookingDate()))
            .satisfies(e -> assertThat(e.getStartDate()).as("check startDate").isEqualTo(actual.getStartDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(
                e ->
                    assertThat(e.getTotalPrice())
                        .as("check totalPrice")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getTotalPrice())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBookingUpdatableRelationshipsEquals(Booking expected, Booking actual) {
        assertThat(expected)
            .as("Verify Booking relationships")
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()));
    }
}
