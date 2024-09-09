package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LoyaltyTransactionCriteriaTest {

    @Test
    void newLoyaltyTransactionCriteriaHasAllFiltersNullTest() {
        var loyaltyTransactionCriteria = new LoyaltyTransactionCriteria();
        assertThat(loyaltyTransactionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void loyaltyTransactionCriteriaFluentMethodsCreatesFiltersTest() {
        var loyaltyTransactionCriteria = new LoyaltyTransactionCriteria();

        setAllFilters(loyaltyTransactionCriteria);

        assertThat(loyaltyTransactionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void loyaltyTransactionCriteriaCopyCreatesNullFilterTest() {
        var loyaltyTransactionCriteria = new LoyaltyTransactionCriteria();
        var copy = loyaltyTransactionCriteria.copy();

        assertThat(loyaltyTransactionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(loyaltyTransactionCriteria)
        );
    }

    @Test
    void loyaltyTransactionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var loyaltyTransactionCriteria = new LoyaltyTransactionCriteria();
        setAllFilters(loyaltyTransactionCriteria);

        var copy = loyaltyTransactionCriteria.copy();

        assertThat(loyaltyTransactionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(loyaltyTransactionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var loyaltyTransactionCriteria = new LoyaltyTransactionCriteria();

        assertThat(loyaltyTransactionCriteria).hasToString("LoyaltyTransactionCriteria{}");
    }

    private static void setAllFilters(LoyaltyTransactionCriteria loyaltyTransactionCriteria) {
        loyaltyTransactionCriteria.id();
        loyaltyTransactionCriteria.date();
        loyaltyTransactionCriteria.points();
        loyaltyTransactionCriteria.transactionType();
        loyaltyTransactionCriteria.description();
        loyaltyTransactionCriteria.customerId();
        loyaltyTransactionCriteria.distinct();
    }

    private static Condition<LoyaltyTransactionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getPoints()) &&
                condition.apply(criteria.getTransactionType()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LoyaltyTransactionCriteria> copyFiltersAre(
        LoyaltyTransactionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getPoints(), copy.getPoints()) &&
                condition.apply(criteria.getTransactionType(), copy.getTransactionType()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
