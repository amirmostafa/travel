package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TestimonialCriteriaTest {

    @Test
    void newTestimonialCriteriaHasAllFiltersNullTest() {
        var testimonialCriteria = new TestimonialCriteria();
        assertThat(testimonialCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void testimonialCriteriaFluentMethodsCreatesFiltersTest() {
        var testimonialCriteria = new TestimonialCriteria();

        setAllFilters(testimonialCriteria);

        assertThat(testimonialCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void testimonialCriteriaCopyCreatesNullFilterTest() {
        var testimonialCriteria = new TestimonialCriteria();
        var copy = testimonialCriteria.copy();

        assertThat(testimonialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(testimonialCriteria)
        );
    }

    @Test
    void testimonialCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var testimonialCriteria = new TestimonialCriteria();
        setAllFilters(testimonialCriteria);

        var copy = testimonialCriteria.copy();

        assertThat(testimonialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(testimonialCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var testimonialCriteria = new TestimonialCriteria();

        assertThat(testimonialCriteria).hasToString("TestimonialCriteria{}");
    }

    private static void setAllFilters(TestimonialCriteria testimonialCriteria) {
        testimonialCriteria.id();
        testimonialCriteria.authorName();
        testimonialCriteria.content();
        testimonialCriteria.rating();
        testimonialCriteria.date();
        testimonialCriteria.hotelId();
        testimonialCriteria.distinct();
    }

    private static Condition<TestimonialCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAuthorName()) &&
                condition.apply(criteria.getContent()) &&
                condition.apply(criteria.getRating()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getHotelId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TestimonialCriteria> copyFiltersAre(TestimonialCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAuthorName(), copy.getAuthorName()) &&
                condition.apply(criteria.getContent(), copy.getContent()) &&
                condition.apply(criteria.getRating(), copy.getRating()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getHotelId(), copy.getHotelId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
