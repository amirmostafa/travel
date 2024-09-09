package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CurrencyCriteriaTest {

    @Test
    void newCurrencyCriteriaHasAllFiltersNullTest() {
        var currencyCriteria = new CurrencyCriteria();
        assertThat(currencyCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void currencyCriteriaFluentMethodsCreatesFiltersTest() {
        var currencyCriteria = new CurrencyCriteria();

        setAllFilters(currencyCriteria);

        assertThat(currencyCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void currencyCriteriaCopyCreatesNullFilterTest() {
        var currencyCriteria = new CurrencyCriteria();
        var copy = currencyCriteria.copy();

        assertThat(currencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(currencyCriteria)
        );
    }

    @Test
    void currencyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var currencyCriteria = new CurrencyCriteria();
        setAllFilters(currencyCriteria);

        var copy = currencyCriteria.copy();

        assertThat(currencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(currencyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var currencyCriteria = new CurrencyCriteria();

        assertThat(currencyCriteria).hasToString("CurrencyCriteria{}");
    }

    private static void setAllFilters(CurrencyCriteria currencyCriteria) {
        currencyCriteria.id();
        currencyCriteria.code();
        currencyCriteria.name();
        currencyCriteria.symbol();
        currencyCriteria.exchangeRate();
        currencyCriteria.isDefault();
        currencyCriteria.distinct();
    }

    private static Condition<CurrencyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSymbol()) &&
                condition.apply(criteria.getExchangeRate()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CurrencyCriteria> copyFiltersAre(CurrencyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSymbol(), copy.getSymbol()) &&
                condition.apply(criteria.getExchangeRate(), copy.getExchangeRate()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
