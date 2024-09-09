package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgencyCriteriaTest {

    @Test
    void newAgencyCriteriaHasAllFiltersNullTest() {
        var agencyCriteria = new AgencyCriteria();
        assertThat(agencyCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void agencyCriteriaFluentMethodsCreatesFiltersTest() {
        var agencyCriteria = new AgencyCriteria();

        setAllFilters(agencyCriteria);

        assertThat(agencyCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void agencyCriteriaCopyCreatesNullFilterTest() {
        var agencyCriteria = new AgencyCriteria();
        var copy = agencyCriteria.copy();

        assertThat(agencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(agencyCriteria)
        );
    }

    @Test
    void agencyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agencyCriteria = new AgencyCriteria();
        setAllFilters(agencyCriteria);

        var copy = agencyCriteria.copy();

        assertThat(agencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(agencyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agencyCriteria = new AgencyCriteria();

        assertThat(agencyCriteria).hasToString("AgencyCriteria{}");
    }

    private static void setAllFilters(AgencyCriteria agencyCriteria) {
        agencyCriteria.id();
        agencyCriteria.name();
        agencyCriteria.address();
        agencyCriteria.contactNumber();
        agencyCriteria.email();
        agencyCriteria.website();
        agencyCriteria.distinct();
    }

    private static Condition<AgencyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getContactNumber()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgencyCriteria> copyFiltersAre(AgencyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getContactNumber(), copy.getContactNumber()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
