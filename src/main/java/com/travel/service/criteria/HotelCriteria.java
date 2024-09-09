package com.travel.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.travel.domain.Hotel} entity. This class is used
 * in {@link com.travel.web.rest.HotelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hotels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HotelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private IntegerFilter starRating;

    private StringFilter contactNumber;

    private StringFilter email;

    private StringFilter countryCode;

    private StringFilter cityCode;

    private StringFilter imageUrl;

    private LongFilter roomId;

    private LongFilter testimonialId;

    private Boolean distinct;

    public HotelCriteria() {}

    public HotelCriteria(HotelCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.starRating = other.optionalStarRating().map(IntegerFilter::copy).orElse(null);
        this.contactNumber = other.optionalContactNumber().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.countryCode = other.optionalCountryCode().map(StringFilter::copy).orElse(null);
        this.cityCode = other.optionalCityCode().map(StringFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.roomId = other.optionalRoomId().map(LongFilter::copy).orElse(null);
        this.testimonialId = other.optionalTestimonialId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HotelCriteria copy() {
        return new HotelCriteria(this);
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

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public IntegerFilter getStarRating() {
        return starRating;
    }

    public Optional<IntegerFilter> optionalStarRating() {
        return Optional.ofNullable(starRating);
    }

    public IntegerFilter starRating() {
        if (starRating == null) {
            setStarRating(new IntegerFilter());
        }
        return starRating;
    }

    public void setStarRating(IntegerFilter starRating) {
        this.starRating = starRating;
    }

    public StringFilter getContactNumber() {
        return contactNumber;
    }

    public Optional<StringFilter> optionalContactNumber() {
        return Optional.ofNullable(contactNumber);
    }

    public StringFilter contactNumber() {
        if (contactNumber == null) {
            setContactNumber(new StringFilter());
        }
        return contactNumber;
    }

    public void setContactNumber(StringFilter contactNumber) {
        this.contactNumber = contactNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getCountryCode() {
        return countryCode;
    }

    public Optional<StringFilter> optionalCountryCode() {
        return Optional.ofNullable(countryCode);
    }

    public StringFilter countryCode() {
        if (countryCode == null) {
            setCountryCode(new StringFilter());
        }
        return countryCode;
    }

    public void setCountryCode(StringFilter countryCode) {
        this.countryCode = countryCode;
    }

    public StringFilter getCityCode() {
        return cityCode;
    }

    public Optional<StringFilter> optionalCityCode() {
        return Optional.ofNullable(cityCode);
    }

    public StringFilter cityCode() {
        if (cityCode == null) {
            setCityCode(new StringFilter());
        }
        return cityCode;
    }

    public void setCityCode(StringFilter cityCode) {
        this.cityCode = cityCode;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
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

    public LongFilter getTestimonialId() {
        return testimonialId;
    }

    public Optional<LongFilter> optionalTestimonialId() {
        return Optional.ofNullable(testimonialId);
    }

    public LongFilter testimonialId() {
        if (testimonialId == null) {
            setTestimonialId(new LongFilter());
        }
        return testimonialId;
    }

    public void setTestimonialId(LongFilter testimonialId) {
        this.testimonialId = testimonialId;
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
        final HotelCriteria that = (HotelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(starRating, that.starRating) &&
            Objects.equals(contactNumber, that.contactNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(countryCode, that.countryCode) &&
            Objects.equals(cityCode, that.cityCode) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(roomId, that.roomId) &&
            Objects.equals(testimonialId, that.testimonialId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            address,
            starRating,
            contactNumber,
            email,
            countryCode,
            cityCode,
            imageUrl,
            roomId,
            testimonialId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HotelCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalStarRating().map(f -> "starRating=" + f + ", ").orElse("") +
            optionalContactNumber().map(f -> "contactNumber=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalCountryCode().map(f -> "countryCode=" + f + ", ").orElse("") +
            optionalCityCode().map(f -> "cityCode=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalRoomId().map(f -> "roomId=" + f + ", ").orElse("") +
            optionalTestimonialId().map(f -> "testimonialId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
