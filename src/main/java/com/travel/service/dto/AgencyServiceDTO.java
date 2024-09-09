package com.travel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.travel.domain.AgencyService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyServiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String icon;

    @NotNull
    @Size(max = 65535)
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgencyServiceDTO)) {
            return false;
        }

        AgencyServiceDTO agencyServiceDTO = (AgencyServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agencyServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyServiceDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", icon='" + getIcon() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
