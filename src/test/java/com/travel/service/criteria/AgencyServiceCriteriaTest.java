package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgencyServiceCriteriaTest {

    @Test
    void newAgencyServiceCriteriaHasAllFiltersNullTest() {
        var agencyServiceCriteria = new AgencyServiceCriteria();
        assertThat(agencyServiceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void agencyServiceCriteriaFluentMethodsCreatesFiltersTest() {
        var agencyServiceCriteria = new AgencyServiceCriteria();

        setAllFilters(agencyServiceCriteria);

        assertThat(agencyServiceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void agencyServiceCriteriaCopyCreatesNullFilterTest() {
        var agencyServiceCriteria = new AgencyServiceCriteria();
        var copy = agencyServiceCriteria.copy();

        assertThat(agencyServiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(agencyServiceCriteria)
        );
    }

    @Test
    void agencyServiceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agencyServiceCriteria = new AgencyServiceCriteria();
        setAllFilters(agencyServiceCriteria);

        var copy = agencyServiceCriteria.copy();

        assertThat(agencyServiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(agencyServiceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agencyServiceCriteria = new AgencyServiceCriteria();

        assertThat(agencyServiceCriteria).hasToString("AgencyServiceCriteria{}");
    }

    private static void setAllFilters(AgencyServiceCriteria agencyServiceCriteria) {
        agencyServiceCriteria.id();
        agencyServiceCriteria.title();
        agencyServiceCriteria.icon();
        agencyServiceCriteria.content();
        agencyServiceCriteria.distinct();
    }

    private static Condition<AgencyServiceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getIcon()) &&
                condition.apply(criteria.getContent()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgencyServiceCriteria> copyFiltersAre(
        AgencyServiceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getIcon(), copy.getIcon()) &&
                condition.apply(criteria.getContent(), copy.getContent()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
