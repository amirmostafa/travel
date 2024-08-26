package com.travel.domain;

import static com.travel.domain.AgencyTestSamples.*;
import static com.travel.domain.TourPackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TourPackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TourPackage.class);
        TourPackage tourPackage1 = getTourPackageSample1();
        TourPackage tourPackage2 = new TourPackage();
        assertThat(tourPackage1).isNotEqualTo(tourPackage2);

        tourPackage2.setId(tourPackage1.getId());
        assertThat(tourPackage1).isEqualTo(tourPackage2);

        tourPackage2 = getTourPackageSample2();
        assertThat(tourPackage1).isNotEqualTo(tourPackage2);
    }

    @Test
    void agencyTest() {
        TourPackage tourPackage = getTourPackageRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        tourPackage.setAgency(agencyBack);
        assertThat(tourPackage.getAgency()).isEqualTo(agencyBack);

        tourPackage.agency(null);
        assertThat(tourPackage.getAgency()).isNull();
    }
}
