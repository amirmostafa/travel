package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AboutUsCriteriaTest {

    @Test
    void newAboutUsCriteriaHasAllFiltersNullTest() {
        var aboutUsCriteria = new AboutUsCriteria();
        assertThat(aboutUsCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void aboutUsCriteriaFluentMethodsCreatesFiltersTest() {
        var aboutUsCriteria = new AboutUsCriteria();

        setAllFilters(aboutUsCriteria);

        assertThat(aboutUsCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void aboutUsCriteriaCopyCreatesNullFilterTest() {
        var aboutUsCriteria = new AboutUsCriteria();
        var copy = aboutUsCriteria.copy();

        assertThat(aboutUsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(aboutUsCriteria)
        );
    }

    @Test
    void aboutUsCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aboutUsCriteria = new AboutUsCriteria();
        setAllFilters(aboutUsCriteria);

        var copy = aboutUsCriteria.copy();

        assertThat(aboutUsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(aboutUsCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aboutUsCriteria = new AboutUsCriteria();

        assertThat(aboutUsCriteria).hasToString("AboutUsCriteria{}");
    }

    private static void setAllFilters(AboutUsCriteria aboutUsCriteria) {
        aboutUsCriteria.id();
        aboutUsCriteria.description();
        aboutUsCriteria.contactDetails();
        aboutUsCriteria.additionalInfo();
        aboutUsCriteria.distinct();
    }

    private static Condition<AboutUsCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getContactDetails()) &&
                condition.apply(criteria.getAdditionalInfo()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AboutUsCriteria> copyFiltersAre(AboutUsCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getContactDetails(), copy.getContactDetails()) &&
                condition.apply(criteria.getAdditionalInfo(), copy.getAdditionalInfo()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
