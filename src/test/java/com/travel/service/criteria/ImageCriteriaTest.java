package com.travel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageCriteriaTest {

    @Test
    void newImageCriteriaHasAllFiltersNullTest() {
        var imageCriteria = new ImageCriteria();
        assertThat(imageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void imageCriteriaFluentMethodsCreatesFiltersTest() {
        var imageCriteria = new ImageCriteria();

        setAllFilters(imageCriteria);

        assertThat(imageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void imageCriteriaCopyCreatesNullFilterTest() {
        var imageCriteria = new ImageCriteria();
        var copy = imageCriteria.copy();

        assertThat(imageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(imageCriteria)
        );
    }

    @Test
    void imageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageCriteria = new ImageCriteria();
        setAllFilters(imageCriteria);

        var copy = imageCriteria.copy();

        assertThat(imageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(imageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageCriteria = new ImageCriteria();

        assertThat(imageCriteria).hasToString("ImageCriteria{}");
    }

    private static void setAllFilters(ImageCriteria imageCriteria) {
        imageCriteria.id();
        imageCriteria.url();
        imageCriteria.description();
        imageCriteria.roomId();
        imageCriteria.distinct();
    }

    private static Condition<ImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getRoomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageCriteria> copyFiltersAre(ImageCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getRoomId(), copy.getRoomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
