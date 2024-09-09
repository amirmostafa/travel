package com.travel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AboutUsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AboutUsDTO.class);
        AboutUsDTO aboutUsDTO1 = new AboutUsDTO();
        aboutUsDTO1.setId(1L);
        AboutUsDTO aboutUsDTO2 = new AboutUsDTO();
        assertThat(aboutUsDTO1).isNotEqualTo(aboutUsDTO2);
        aboutUsDTO2.setId(aboutUsDTO1.getId());
        assertThat(aboutUsDTO1).isEqualTo(aboutUsDTO2);
        aboutUsDTO2.setId(2L);
        assertThat(aboutUsDTO1).isNotEqualTo(aboutUsDTO2);
        aboutUsDTO1.setId(null);
        assertThat(aboutUsDTO1).isNotEqualTo(aboutUsDTO2);
    }
}
