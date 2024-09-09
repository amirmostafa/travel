package com.travel.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.travel.domain.LoyaltyProgram} entity. This class is used
 * in {@link com.travel.web.rest.LoyaltyProgramResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /loyalty-programs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyProgramCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter pointsPerDollar;

    private IntegerFilter rewardThreshold;

    private Boolean distinct;

    public LoyaltyProgramCriteria() {}

    public LoyaltyProgramCriteria(LoyaltyProgramCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.pointsPerDollar = other.optionalPointsPerDollar().map(IntegerFilter::copy).orElse(null);
        this.rewardThreshold = other.optionalRewardThreshold().map(IntegerFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LoyaltyProgramCriteria copy() {
        return new LoyaltyProgramCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getPointsPerDollar() {
        return pointsPerDollar;
    }

    public Optional<IntegerFilter> optionalPointsPerDollar() {
        return Optional.ofNullable(pointsPerDollar);
    }

    public IntegerFilter pointsPerDollar() {
        if (pointsPerDollar == null) {
            setPointsPerDollar(new IntegerFilter());
        }
        return pointsPerDollar;
    }

    public void setPointsPerDollar(IntegerFilter pointsPerDollar) {
        this.pointsPerDollar = pointsPerDollar;
    }

    public IntegerFilter getRewardThreshold() {
        return rewardThreshold;
    }

    public Optional<IntegerFilter> optionalRewardThreshold() {
        return Optional.ofNullable(rewardThreshold);
    }

    public IntegerFilter rewardThreshold() {
        if (rewardThreshold == null) {
            setRewardThreshold(new IntegerFilter());
        }
        return rewardThreshold;
    }

    public void setRewardThreshold(IntegerFilter rewardThreshold) {
        this.rewardThreshold = rewardThreshold;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LoyaltyProgramCriteria that = (LoyaltyProgramCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(pointsPerDollar, that.pointsPerDollar) &&
            Objects.equals(rewardThreshold, that.rewardThreshold) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, pointsPerDollar, rewardThreshold, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyProgramCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPointsPerDollar().map(f -> "pointsPerDollar=" + f + ", ").orElse("") +
            optionalRewardThreshold().map(f -> "rewardThreshold=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
