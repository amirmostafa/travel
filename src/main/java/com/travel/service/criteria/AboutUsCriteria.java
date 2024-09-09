package com.travel.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.travel.domain.AboutUs} entity. This class is used
 * in {@link com.travel.web.rest.AboutUsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aboutuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AboutUsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter contactDetails;

    private StringFilter additionalInfo;

    private Boolean distinct;

    public AboutUsCriteria() {}

    public AboutUsCriteria(AboutUsCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.contactDetails = other.optionalContactDetails().map(StringFilter::copy).orElse(null);
        this.additionalInfo = other.optionalAdditionalInfo().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AboutUsCriteria copy() {
        return new AboutUsCriteria(this);
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

    public StringFilter getContactDetails() {
        return contactDetails;
    }

    public Optional<StringFilter> optionalContactDetails() {
        return Optional.ofNullable(contactDetails);
    }

    public StringFilter contactDetails() {
        if (contactDetails == null) {
            setContactDetails(new StringFilter());
        }
        return contactDetails;
    }

    public void setContactDetails(StringFilter contactDetails) {
        this.contactDetails = contactDetails;
    }

    public StringFilter getAdditionalInfo() {
        return additionalInfo;
    }

    public Optional<StringFilter> optionalAdditionalInfo() {
        return Optional.ofNullable(additionalInfo);
    }

    public StringFilter additionalInfo() {
        if (additionalInfo == null) {
            setAdditionalInfo(new StringFilter());
        }
        return additionalInfo;
    }

    public void setAdditionalInfo(StringFilter additionalInfo) {
        this.additionalInfo = additionalInfo;
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
        final AboutUsCriteria that = (AboutUsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(contactDetails, that.contactDetails) &&
            Objects.equals(additionalInfo, that.additionalInfo) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, contactDetails, additionalInfo, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AboutUsCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalContactDetails().map(f -> "contactDetails=" + f + ", ").orElse("") +
            optionalAdditionalInfo().map(f -> "additionalInfo=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
