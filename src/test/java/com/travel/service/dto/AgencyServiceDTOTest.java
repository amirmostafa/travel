package com.travel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgencyServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyServiceDTO.class);
        AgencyServiceDTO agencyServiceDTO1 = new AgencyServiceDTO();
        agencyServiceDTO1.setId(1L);
        AgencyServiceDTO agencyServiceDTO2 = new AgencyServiceDTO();
        assertThat(agencyServiceDTO1).isNotEqualTo(agencyServiceDTO2);
        agencyServiceDTO2.setId(agencyServiceDTO1.getId());
        assertThat(agencyServiceDTO1).isEqualTo(agencyServiceDTO2);
        agencyServiceDTO2.setId(2L);
        assertThat(agencyServiceDTO1).isNotEqualTo(agencyServiceDTO2);
        agencyServiceDTO1.setId(null);
        assertThat(agencyServiceDTO1).isNotEqualTo(agencyServiceDTO2);
    }
}
