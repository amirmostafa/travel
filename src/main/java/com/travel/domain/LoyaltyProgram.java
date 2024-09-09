package com.travel.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LoyaltyProgram.
 */
@Entity
@Table(name = "loyalty_program")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Min(value = 0)
    @Column(name = "points_per_dollar", nullable = false)
    private Integer pointsPerDollar;

    @NotNull
    @Min(value = 0)
    @Column(name = "reward_threshold", nullable = false)
    private Integer rewardThreshold;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoyaltyProgram id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public LoyaltyProgram name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public LoyaltyProgram description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPointsPerDollar() {
        return this.pointsPerDollar;
    }

    public LoyaltyProgram pointsPerDollar(Integer pointsPerDollar) {
        this.setPointsPerDollar(pointsPerDollar);
        return this;
    }

    public void setPointsPerDollar(Integer pointsPerDollar) {
        this.pointsPerDollar = pointsPerDollar;
    }

    public Integer getRewardThreshold() {
        return this.rewardThreshold;
    }

    public LoyaltyProgram rewardThreshold(Integer rewardThreshold) {
        this.setRewardThreshold(rewardThreshold);
        return this;
    }

    public void setRewardThreshold(Integer rewardThreshold) {
        this.rewardThreshold = rewardThreshold;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoyaltyProgram)) {
            return false;
        }
        return getId() != null && getId().equals(((LoyaltyProgram) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyProgram{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", pointsPerDollar=" + getPointsPerDollar() +
            ", rewardThreshold=" + getRewardThreshold() +
            "}";
    }
}
