package com.travel.domain;

import static com.travel.domain.AboutUsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AboutUsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AboutUs.class);
        AboutUs aboutUs1 = getAboutUsSample1();
        AboutUs aboutUs2 = new AboutUs();
        assertThat(aboutUs1).isNotEqualTo(aboutUs2);

        aboutUs2.setId(aboutUs1.getId());
        assertThat(aboutUs1).isEqualTo(aboutUs2);

        aboutUs2 = getAboutUsSample2();
        assertThat(aboutUs1).isNotEqualTo(aboutUs2);
    }
}
