package com.travel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.travel.domain.RoomPrice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomPriceDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    private RoomDTO room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomPriceDTO)) {
            return false;
        }

        RoomPriceDTO roomPriceDTO = (RoomPriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomPriceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomPriceDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", room=" + getRoom() +
            "}";
    }
}
