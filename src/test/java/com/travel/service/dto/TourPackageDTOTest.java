package com.travel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TourPackageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TourPackageDTO.class);
        TourPackageDTO tourPackageDTO1 = new TourPackageDTO();
        tourPackageDTO1.setId(1L);
        TourPackageDTO tourPackageDTO2 = new TourPackageDTO();
        assertThat(tourPackageDTO1).isNotEqualTo(tourPackageDTO2);
        tourPackageDTO2.setId(tourPackageDTO1.getId());
        assertThat(tourPackageDTO1).isEqualTo(tourPackageDTO2);
        tourPackageDTO2.setId(2L);
        assertThat(tourPackageDTO1).isNotEqualTo(tourPackageDTO2);
        tourPackageDTO1.setId(null);
        assertThat(tourPackageDTO1).isNotEqualTo(tourPackageDTO2);
    }
}
