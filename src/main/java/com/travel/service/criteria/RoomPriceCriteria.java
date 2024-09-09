package com.travel.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.travel.domain.RoomPrice} entity. This class is used
 * in {@link com.travel.web.rest.RoomPriceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /room-prices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomPriceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter price;

    private LocalDateFilter fromDate;

    private LocalDateFilter toDate;

    private LongFilter roomId;

    private Boolean distinct;

    public RoomPriceCriteria() {}

    public RoomPriceCriteria(RoomPriceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(LocalDateFilter::copy).orElse(null);
        this.toDate = other.optionalToDate().map(LocalDateFilter::copy).orElse(null);
        this.roomId = other.optionalRoomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RoomPriceCriteria copy() {
        return new RoomPriceCriteria(this);
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

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public LocalDateFilter getFromDate() {
        return fromDate;
    }

    public Optional<LocalDateFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public LocalDateFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new LocalDateFilter());
        }
        return fromDate;
    }

    public void setFromDate(LocalDateFilter fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateFilter getToDate() {
        return toDate;
    }

    public Optional<LocalDateFilter> optionalToDate() {
        return Optional.ofNullable(toDate);
    }

    public LocalDateFilter toDate() {
        if (toDate == null) {
            setToDate(new LocalDateFilter());
        }
        return toDate;
    }

    public void setToDate(LocalDateFilter toDate) {
        this.toDate = toDate;
    }

    public LongFilter getRoomId() {
        return roomId;
    }

    public Optional<LongFilter> optionalRoomId() {
        return Optional.ofNullable(roomId);
    }

    public LongFilter roomId() {
        if (roomId == null) {
            setRoomId(new LongFilter());
        }
        return roomId;
    }

    public void setRoomId(LongFilter roomId) {
        this.roomId = roomId;
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
        final RoomPriceCriteria that = (RoomPriceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(roomId, that.roomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, fromDate, toDate, roomId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomPriceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalToDate().map(f -> "toDate=" + f + ", ").orElse("") +
            optionalRoomId().map(f -> "roomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
