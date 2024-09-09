package com.travel.domain;

import static com.travel.domain.LoyaltyProgramTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoyaltyProgramTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoyaltyProgram.class);
        LoyaltyProgram loyaltyProgram1 = getLoyaltyProgramSample1();
        LoyaltyProgram loyaltyProgram2 = new LoyaltyProgram();
        assertThat(loyaltyProgram1).isNotEqualTo(loyaltyProgram2);

        loyaltyProgram2.setId(loyaltyProgram1.getId());
        assertThat(loyaltyProgram1).isEqualTo(loyaltyProgram2);

        loyaltyProgram2 = getLoyaltyProgramSample2();
        assertThat(loyaltyProgram1).isNotEqualTo(loyaltyProgram2);
    }
}
