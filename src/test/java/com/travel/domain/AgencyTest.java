package com.travel.domain;

import static com.travel.domain.AgencyTestSamples.*;
import static com.travel.domain.TourPackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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

    @Test
    void tourPackageTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        TourPackage tourPackageBack = getTourPackageRandomSampleGenerator();

        agency.addTourPackage(tourPackageBack);
        assertThat(agency.getTourPackages()).containsOnly(tourPackageBack);
        assertThat(tourPackageBack.getAgency()).isEqualTo(agency);

        agency.removeTourPackage(tourPackageBack);
        assertThat(agency.getTourPackages()).doesNotContain(tourPackageBack);
        assertThat(tourPackageBack.getAgency()).isNull();

        agency.tourPackages(new HashSet<>(Set.of(tourPackageBack)));
        assertThat(agency.getTourPackages()).containsOnly(tourPackageBack);
        assertThat(tourPackageBack.getAgency()).isEqualTo(agency);

        agency.setTourPackages(new HashSet<>());
        assertThat(agency.getTourPackages()).doesNotContain(tourPackageBack);
        assertThat(tourPackageBack.getAgency()).isNull();
    }
}
