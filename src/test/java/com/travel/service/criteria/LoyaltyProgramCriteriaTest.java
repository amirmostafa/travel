package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LoyaltyProgramCriteriaTest {

    @Test
    void newLoyaltyProgramCriteriaHasAllFiltersNullTest() {
        var loyaltyProgramCriteria = new LoyaltyProgramCriteria();
        assertThat(loyaltyProgramCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void loyaltyProgramCriteriaFluentMethodsCreatesFiltersTest() {
        var loyaltyProgramCriteria = new LoyaltyProgramCriteria();

        setAllFilters(loyaltyProgramCriteria);

        assertThat(loyaltyProgramCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void loyaltyProgramCriteriaCopyCreatesNullFilterTest() {
        var loyaltyProgramCriteria = new LoyaltyProgramCriteria();
        var copy = loyaltyProgramCriteria.copy();

        assertThat(loyaltyProgramCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(loyaltyProgramCriteria)
        );
    }

    @Test
    void loyaltyProgramCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var loyaltyProgramCriteria = new LoyaltyProgramCriteria();
        setAllFilters(loyaltyProgramCriteria);

        var copy = loyaltyProgramCriteria.copy();

        assertThat(loyaltyProgramCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(loyaltyProgramCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var loyaltyProgramCriteria = new LoyaltyProgramCriteria();

        assertThat(loyaltyProgramCriteria).hasToString("LoyaltyProgramCriteria{}");
    }

    private static void setAllFilters(LoyaltyProgramCriteria loyaltyProgramCriteria) {
        loyaltyProgramCriteria.id();
        loyaltyProgramCriteria.name();
        loyaltyProgramCriteria.description();
        loyaltyProgramCriteria.pointsPerDollar();
        loyaltyProgramCriteria.rewardThreshold();
        loyaltyProgramCriteria.distinct();
    }

    private static Condition<LoyaltyProgramCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPointsPerDollar()) &&
                condition.apply(criteria.getRewardThreshold()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LoyaltyProgramCriteria> copyFiltersAre(
        LoyaltyProgramCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPointsPerDollar(), copy.getPointsPerDollar()) &&
                condition.apply(criteria.getRewardThreshold(), copy.getRewardThreshold()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
