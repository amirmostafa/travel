package com.travel.domain;

import static com.travel.domain.AgencyServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgencyServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyService.class);
        AgencyService agencyService1 = getAgencyServiceSample1();
        AgencyService agencyService2 = new AgencyService();
        assertThat(agencyService1).isNotEqualTo(agencyService2);

        agencyService2.setId(agencyService1.getId());
        assertThat(agencyService1).isEqualTo(agencyService2);

        agencyService2 = getAgencyServiceSample2();
        assertThat(agencyService1).isNotEqualTo(agencyService2);
    }
}
