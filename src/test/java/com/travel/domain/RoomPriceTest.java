package com.travel.domain;

import static com.travel.domain.RoomPriceTestSamples.*;
import static com.travel.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomPrice.class);
        RoomPrice roomPrice1 = getRoomPriceSample1();
        RoomPrice roomPrice2 = new RoomPrice();
        assertThat(roomPrice1).isNotEqualTo(roomPrice2);

        roomPrice2.setId(roomPrice1.getId());
        assertThat(roomPrice1).isEqualTo(roomPrice2);

        roomPrice2 = getRoomPriceSample2();
        assertThat(roomPrice1).isNotEqualTo(roomPrice2);
    }

    @Test
    void roomTest() {
        RoomPrice roomPrice = getRoomPriceRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        roomPrice.setRoom(roomBack);
        assertThat(roomPrice.getRoom()).isEqualTo(roomBack);

        roomPrice.room(null);
        assertThat(roomPrice.getRoom()).isNull();
    }
}
