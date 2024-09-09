package com.travel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoyaltyProgramDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoyaltyProgramDTO.class);
        LoyaltyProgramDTO loyaltyProgramDTO1 = new LoyaltyProgramDTO();
        loyaltyProgramDTO1.setId(1L);
        LoyaltyProgramDTO loyaltyProgramDTO2 = new LoyaltyProgramDTO();
        assertThat(loyaltyProgramDTO1).isNotEqualTo(loyaltyProgramDTO2);
        loyaltyProgramDTO2.setId(loyaltyProgramDTO1.getId());
        assertThat(loyaltyProgramDTO1).isEqualTo(loyaltyProgramDTO2);
        loyaltyProgramDTO2.setId(2L);
        assertThat(loyaltyProgramDTO1).isNotEqualTo(loyaltyProgramDTO2);
        loyaltyProgramDTO1.setId(null);
        assertThat(loyaltyProgramDTO1).isNotEqualTo(loyaltyProgramDTO2);
    }
}
