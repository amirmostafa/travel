package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HotelCriteriaTest {

    @Test
    void newHotelCriteriaHasAllFiltersNullTest() {
        var hotelCriteria = new HotelCriteria();
        assertThat(hotelCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hotelCriteriaFluentMethodsCreatesFiltersTest() {
        var hotelCriteria = new HotelCriteria();

        setAllFilters(hotelCriteria);

        assertThat(hotelCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hotelCriteriaCopyCreatesNullFilterTest() {
        var hotelCriteria = new HotelCriteria();
        var copy = hotelCriteria.copy();

        assertThat(hotelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hotelCriteria)
        );
    }

    @Test
    void hotelCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hotelCriteria = new HotelCriteria();
        setAllFilters(hotelCriteria);

        var copy = hotelCriteria.copy();

        assertThat(hotelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hotelCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hotelCriteria = new HotelCriteria();

        assertThat(hotelCriteria).hasToString("HotelCriteria{}");
    }

    private static void setAllFilters(HotelCriteria hotelCriteria) {
        hotelCriteria.id();
        hotelCriteria.name();
        hotelCriteria.address();
        hotelCriteria.starRating();
        hotelCriteria.contactNumber();
        hotelCriteria.email();
        hotelCriteria.countryCode();
        hotelCriteria.cityCode();
        hotelCriteria.imageUrl();
        hotelCriteria.roomId();
        hotelCriteria.testimonialId();
        hotelCriteria.distinct();
    }

    private static Condition<HotelCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getStarRating()) &&
                condition.apply(criteria.getContactNumber()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getCountryCode()) &&
                condition.apply(criteria.getCityCode()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getRoomId()) &&
                condition.apply(criteria.getTestimonialId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HotelCriteria> copyFiltersAre(HotelCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getStarRating(), copy.getStarRating()) &&
                condition.apply(criteria.getContactNumber(), copy.getContactNumber()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getCountryCode(), copy.getCountryCode()) &&
                condition.apply(criteria.getCityCode(), copy.getCityCode()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getRoomId(), copy.getRoomId()) &&
                condition.apply(criteria.getTestimonialId(), copy.getTestimonialId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
