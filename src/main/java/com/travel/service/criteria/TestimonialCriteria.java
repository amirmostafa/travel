package com.travel.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.travel.domain.Testimonial} entity. This class is used
 * in {@link com.travel.web.rest.TestimonialResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /testimonials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestimonialCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter authorName;

    private StringFilter content;

    private IntegerFilter rating;

    private LocalDateFilter date;

    private LongFilter hotelId;

    private Boolean distinct;

    public TestimonialCriteria() {}

    public TestimonialCriteria(TestimonialCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.authorName = other.optionalAuthorName().map(StringFilter::copy).orElse(null);
        this.content = other.optionalContent().map(StringFilter::copy).orElse(null);
        this.rating = other.optionalRating().map(IntegerFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.hotelId = other.optionalHotelId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TestimonialCriteria copy() {
        return new TestimonialCriteria(this);
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

    public StringFilter getAuthorName() {
        return authorName;
    }

    public Optional<StringFilter> optionalAuthorName() {
        return Optional.ofNullable(authorName);
    }

    public StringFilter authorName() {
        if (authorName == null) {
            setAuthorName(new StringFilter());
        }
        return authorName;
    }

    public void setAuthorName(StringFilter authorName) {
        this.authorName = authorName;
    }

    public StringFilter getContent() {
        return content;
    }

    public Optional<StringFilter> optionalContent() {
        return Optional.ofNullable(content);
    }

    public StringFilter content() {
        if (content == null) {
            setContent(new StringFilter());
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public IntegerFilter getRating() {
        return rating;
    }

    public Optional<IntegerFilter> optionalRating() {
        return Optional.ofNullable(rating);
    }

    public IntegerFilter rating() {
        if (rating == null) {
            setRating(new IntegerFilter());
        }
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public Optional<LocalDateFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public LocalDateFilter date() {
        if (date == null) {
            setDate(new LocalDateFilter());
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getHotelId() {
        return hotelId;
    }

    public Optional<LongFilter> optionalHotelId() {
        return Optional.ofNullable(hotelId);
    }

    public LongFilter hotelId() {
        if (hotelId == null) {
            setHotelId(new LongFilter());
        }
        return hotelId;
    }

    public void setHotelId(LongFilter hotelId) {
        this.hotelId = hotelId;
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
        final TestimonialCriteria that = (TestimonialCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(authorName, that.authorName) &&
            Objects.equals(content, that.content) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(date, that.date) &&
            Objects.equals(hotelId, that.hotelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorName, content, rating, date, hotelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestimonialCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAuthorName().map(f -> "authorName=" + f + ", ").orElse("") +
            optionalContent().map(f -> "content=" + f + ", ").orElse("") +
            optionalRating().map(f -> "rating=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalHotelId().map(f -> "hotelId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
