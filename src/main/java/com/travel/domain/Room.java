package com.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.domain.enumeration.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RoomType type;

    @Lob
    @Column(name = "description")
    private String description;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "discount_percentage")
    private Double discountPercentage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "room" }, allowSetters = true)
    private Set<RoomPrice> roomPrices = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rooms" }, allowSetters = true)
    private Hotel hotel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public Room roomNumber(String roomNumber) {
        this.setRoomNumber(roomNumber);
        return this;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return this.type;
    }

    public Room type(RoomType type) {
        this.setType(type);
        return this;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Room description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscountPercentage() {
        return this.discountPercentage;
    }

    public Room discountPercentage(Double discountPercentage) {
        this.setDiscountPercentage(discountPercentage);
        return this;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Set<RoomPrice> getRoomPrices() {
        return this.roomPrices;
    }

    public void setRoomPrices(Set<RoomPrice> roomPrices) {
        if (this.roomPrices != null) {
            this.roomPrices.forEach(i -> i.setRoom(null));
        }
        if (roomPrices != null) {
            roomPrices.forEach(i -> i.setRoom(this));
        }
        this.roomPrices = roomPrices;
    }

    public Room roomPrices(Set<RoomPrice> roomPrices) {
        this.setRoomPrices(roomPrices);
        return this;
    }

    public Room addRoomPrice(RoomPrice roomPrice) {
        this.roomPrices.add(roomPrice);
        roomPrice.setRoom(this);
        return this;
    }

    public Room removeRoomPrice(RoomPrice roomPrice) {
        this.roomPrices.remove(roomPrice);
        roomPrice.setRoom(null);
        return this;
    }

    public Hotel getHotel() {
        return this.hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Room hotel(Hotel hotel) {
        this.setHotel(hotel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return getId() != null && getId().equals(((Room) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            "}";
    }
}
