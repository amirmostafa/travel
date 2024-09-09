package com.travel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.travel.domain.AboutUs} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AboutUsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 65535)
    private String description;

    private String contactDetails;

    @Size(max = 65535)
    private String additionalInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AboutUsDTO)) {
            return false;
        }

        AboutUsDTO aboutUsDTO = (AboutUsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aboutUsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AboutUsDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", contactDetails='" + getContactDetails() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            "}";
    }
}
