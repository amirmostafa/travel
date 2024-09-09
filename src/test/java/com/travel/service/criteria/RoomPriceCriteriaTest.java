package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RoomPriceCriteriaTest {

    @Test
    void newRoomPriceCriteriaHasAllFiltersNullTest() {
        var roomPriceCriteria = new RoomPriceCriteria();
        assertThat(roomPriceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void roomPriceCriteriaFluentMethodsCreatesFiltersTest() {
        var roomPriceCriteria = new RoomPriceCriteria();

        setAllFilters(roomPriceCriteria);

        assertThat(roomPriceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void roomPriceCriteriaCopyCreatesNullFilterTest() {
        var roomPriceCriteria = new RoomPriceCriteria();
        var copy = roomPriceCriteria.copy();

        assertThat(roomPriceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(roomPriceCriteria)
        );
    }

    @Test
    void roomPriceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var roomPriceCriteria = new RoomPriceCriteria();
        setAllFilters(roomPriceCriteria);

        var copy = roomPriceCriteria.copy();

        assertThat(roomPriceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(roomPriceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var roomPriceCriteria = new RoomPriceCriteria();

        assertThat(roomPriceCriteria).hasToString("RoomPriceCriteria{}");
    }

    private static void setAllFilters(RoomPriceCriteria roomPriceCriteria) {
        roomPriceCriteria.id();
        roomPriceCriteria.price();
        roomPriceCriteria.fromDate();
        roomPriceCriteria.toDate();
        roomPriceCriteria.roomId();
        roomPriceCriteria.distinct();
    }

    private static Condition<RoomPriceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getToDate()) &&
                condition.apply(criteria.getRoomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RoomPriceCriteria> copyFiltersAre(RoomPriceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getToDate(), copy.getToDate()) &&
                condition.apply(criteria.getRoomId(), copy.getRoomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
