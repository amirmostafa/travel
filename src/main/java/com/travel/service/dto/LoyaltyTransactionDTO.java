package com.travel.service.dto;

import com.travel.domain.enumeration.TransactionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.travel.domain.LoyaltyTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer points;

    @NotNull
    private TransactionType transactionType;

    private String description;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoyaltyTransactionDTO)) {
            return false;
        }

        LoyaltyTransactionDTO loyaltyTransactionDTO = (LoyaltyTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loyaltyTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyTransactionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", points=" + getPoints() +
            ", transactionType='" + getTransactionType() + "'" +
            ", description='" + getDescription() + "'" +
            ", customer=" + getCustomer() +
            "}";
    }
}
