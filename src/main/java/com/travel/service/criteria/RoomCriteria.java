package com.travel.service.criteria;

import com.travel.domain.enumeration.RoomType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.travel.domain.Room} entity. This class is used
 * in {@link com.travel.web.rest.RoomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rooms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RoomType
     */
    public static class RoomTypeFilter extends Filter<RoomType> {

        public RoomTypeFilter() {}

        public RoomTypeFilter(RoomTypeFilter filter) {
            super(filter);
        }

        @Override
        public RoomTypeFilter copy() {
            return new RoomTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter roomNumber;

    private RoomTypeFilter type;

    private StringFilter description;

    private DoubleFilter discountPercentage;

    private LongFilter roomPriceId;

    private LongFilter imagesId;

    private LongFilter hotelId;

    private Boolean distinct;

    public RoomCriteria() {}

    public RoomCriteria(RoomCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.roomNumber = other.optionalRoomNumber().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(RoomTypeFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.discountPercentage = other.optionalDiscountPercentage().map(DoubleFilter::copy).orElse(null);
        this.roomPriceId = other.optionalRoomPriceId().map(LongFilter::copy).orElse(null);
        this.imagesId = other.optionalImagesId().map(LongFilter::copy).orElse(null);
        this.hotelId = other.optionalHotelId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RoomCriteria copy() {
        return new RoomCriteria(this);
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

    public StringFilter getRoomNumber() {
        return roomNumber;
    }

    public Optional<StringFilter> optionalRoomNumber() {
        return Optional.ofNullable(roomNumber);
    }

    public StringFilter roomNumber() {
        if (roomNumber == null) {
            setRoomNumber(new StringFilter());
        }
        return roomNumber;
    }

    public void setRoomNumber(StringFilter roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomTypeFilter getType() {
        return type;
    }

    public Optional<RoomTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public RoomTypeFilter type() {
        if (type == null) {
            setType(new RoomTypeFilter());
        }
        return type;
    }

    public void setType(RoomTypeFilter type) {
        this.type = type;
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

    public DoubleFilter getDiscountPercentage() {
        return discountPercentage;
    }

    public Optional<DoubleFilter> optionalDiscountPercentage() {
        return Optional.ofNullable(discountPercentage);
    }

    public DoubleFilter discountPercentage() {
        if (discountPercentage == null) {
            setDiscountPercentage(new DoubleFilter());
        }
        return discountPercentage;
    }

    public void setDiscountPercentage(DoubleFilter discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LongFilter getRoomPriceId() {
        return roomPriceId;
    }

    public Optional<LongFilter> optionalRoomPriceId() {
        return Optional.ofNullable(roomPriceId);
    }

    public LongFilter roomPriceId() {
        if (roomPriceId == null) {
            setRoomPriceId(new LongFilter());
        }
        return roomPriceId;
    }

    public void setRoomPriceId(LongFilter roomPriceId) {
        this.roomPriceId = roomPriceId;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public Optional<LongFilter> optionalImagesId() {
        return Optional.ofNullable(imagesId);
    }

    public LongFilter imagesId() {
        if (imagesId == null) {
            setImagesId(new LongFilter());
        }
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
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
        final RoomCriteria that = (RoomCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(roomNumber, that.roomNumber) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(discountPercentage, that.discountPercentage) &&
            Objects.equals(roomPriceId, that.roomPriceId) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(hotelId, that.hotelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, type, description, discountPercentage, roomPriceId, imagesId, hotelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRoomNumber().map(f -> "roomNumber=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDiscountPercentage().map(f -> "discountPercentage=" + f + ", ").orElse("") +
            optionalRoomPriceId().map(f -> "roomPriceId=" + f + ", ").orElse("") +
            optionalImagesId().map(f -> "imagesId=" + f + ", ").orElse("") +
            optionalHotelId().map(f -> "hotelId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
