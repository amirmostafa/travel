package com.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Testimonial.
 */
@Entity
@Table(name = "testimonial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Testimonial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "author_name", nullable = false)
    private String authorName;

    @NotNull
    @Size(max = 65535)
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "rating")
    private Integer rating;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testimonial")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rooms", "testimonial" }, allowSetters = true)
    private Set<Hotel> hotels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Testimonial id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public Testimonial authorName(String authorName) {
        this.setAuthorName(authorName);
        return this;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return this.content;
    }

    public Testimonial content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Testimonial rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Testimonial date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Hotel> getHotels() {
        return this.hotels;
    }

    public void setHotels(Set<Hotel> hotels) {
        if (this.hotels != null) {
            this.hotels.forEach(i -> i.setTestimonial(null));
        }
        if (hotels != null) {
            hotels.forEach(i -> i.setTestimonial(this));
        }
        this.hotels = hotels;
    }

    public Testimonial hotels(Set<Hotel> hotels) {
        this.setHotels(hotels);
        return this;
    }

    public Testimonial addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.setTestimonial(this);
        return this;
    }

    public Testimonial removeHotel(Hotel hotel) {
        this.hotels.remove(hotel);
        hotel.setTestimonial(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Testimonial)) {
            return false;
        }
        return getId() != null && getId().equals(((Testimonial) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Testimonial{" +
            "id=" + getId() +
            ", authorName='" + getAuthorName() + "'" +
            ", content='" + getContent() + "'" +
            ", rating=" + getRating() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
