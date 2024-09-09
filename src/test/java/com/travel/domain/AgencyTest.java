package com.travel.domain;

import static com.travel.domain.AgencyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agency.class);
        Agency agency1 = getAgencySample1();
        Agency agency2 = new Agency();
        assertThat(agency1).isNotEqualTo(agency2);

        agency2.setId(agency1.getId());
        assertThat(agency1).isEqualTo(agency2);

        agency2 = getAgencySample2();
        assertThat(agency1).isNotEqualTo(agency2);
    }
}
