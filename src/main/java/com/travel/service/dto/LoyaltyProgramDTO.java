package com.travel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.travel.domain.LoyaltyProgram} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyProgramDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    @Min(value = 0)
    private Integer pointsPerDollar;

    @NotNull
    @Min(value = 0)
    private Integer rewardThreshold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPointsPerDollar() {
        return pointsPerDollar;
    }

    public void setPointsPerDollar(Integer pointsPerDollar) {
        this.pointsPerDollar = pointsPerDollar;
    }

    public Integer getRewardThreshold() {
        return rewardThreshold;
    }

    public void setRewardThreshold(Integer rewardThreshold) {
        this.rewardThreshold = rewardThreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoyaltyProgramDTO)) {
            return false;
        }

        LoyaltyProgramDTO loyaltyProgramDTO = (LoyaltyProgramDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loyaltyProgramDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyProgramDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", pointsPerDollar=" + getPointsPerDollar() +
            ", rewardThreshold=" + getRewardThreshold() +
            "}";
    }
}
