package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TourPackageCriteriaTest {

    @Test
    void newTourPackageCriteriaHasAllFiltersNullTest() {
        var tourPackageCriteria = new TourPackageCriteria();
        assertThat(tourPackageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void tourPackageCriteriaFluentMethodsCreatesFiltersTest() {
        var tourPackageCriteria = new TourPackageCriteria();

        setAllFilters(tourPackageCriteria);

        assertThat(tourPackageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void tourPackageCriteriaCopyCreatesNullFilterTest() {
        var tourPackageCriteria = new TourPackageCriteria();
        var copy = tourPackageCriteria.copy();

        assertThat(tourPackageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(tourPackageCriteria)
        );
    }

    @Test
    void tourPackageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tourPackageCriteria = new TourPackageCriteria();
        setAllFilters(tourPackageCriteria);

        var copy = tourPackageCriteria.copy();

        assertThat(tourPackageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(tourPackageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tourPackageCriteria = new TourPackageCriteria();

        assertThat(tourPackageCriteria).hasToString("TourPackageCriteria{}");
    }

    private static void setAllFilters(TourPackageCriteria tourPackageCriteria) {
        tourPackageCriteria.id();
        tourPackageCriteria.name();
        tourPackageCriteria.description();
        tourPackageCriteria.price();
        tourPackageCriteria.durationDays();
        tourPackageCriteria.available();
        tourPackageCriteria.distinct();
    }

    private static Condition<TourPackageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getDurationDays()) &&
                condition.apply(criteria.getAvailable()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TourPackageCriteria> copyFiltersAre(TourPackageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getDurationDays(), copy.getDurationDays()) &&
                condition.apply(criteria.getAvailable(), copy.getAvailable()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
