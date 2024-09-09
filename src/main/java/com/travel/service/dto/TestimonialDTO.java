package com.travel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.travel.domain.Testimonial} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestimonialDTO implements Serializable {

    private Long id;

    @NotNull
    private String authorName;

    @NotNull
    @Size(max = 65535)
    private String content;

    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    @NotNull
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestimonialDTO)) {
            return false;
        }

        TestimonialDTO testimonialDTO = (TestimonialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testimonialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestimonialDTO{" +
            "id=" + getId() +
            ", authorName='" + getAuthorName() + "'" +
            ", content='" + getContent() + "'" +
            ", rating=" + getRating() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
