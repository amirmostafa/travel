package com.travel.domain;

import static com.travel.domain.ImageTestSamples.*;
import static com.travel.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Image.class);
        Image image1 = getImageSample1();
        Image image2 = new Image();
        assertThat(image1).isNotEqualTo(image2);

        image2.setId(image1.getId());
        assertThat(image1).isEqualTo(image2);

        image2 = getImageSample2();
        assertThat(image1).isNotEqualTo(image2);
    }

    @Test
    void roomTest() {
        Image image = getImageRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        image.setRoom(roomBack);
        assertThat(image.getRoom()).isEqualTo(roomBack);

        image.room(null);
        assertThat(image.getRoom()).isNull();
    }
}
