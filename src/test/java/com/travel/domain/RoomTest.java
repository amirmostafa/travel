package com.travel.domain;

import static com.travel.domain.HotelTestSamples.*;
import static com.travel.domain.ImageTestSamples.*;
import static com.travel.domain.RoomPriceTestSamples.*;
import static com.travel.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Room.class);
        Room room1 = getRoomSample1();
        Room room2 = new Room();
        assertThat(room1).isNotEqualTo(room2);

        room2.setId(room1.getId());
        assertThat(room1).isEqualTo(room2);

        room2 = getRoomSample2();
        assertThat(room1).isNotEqualTo(room2);
    }

    @Test
    void roomPriceTest() {
        Room room = getRoomRandomSampleGenerator();
        RoomPrice roomPriceBack = getRoomPriceRandomSampleGenerator();

        room.addRoomPrice(roomPriceBack);
        assertThat(room.getRoomPrices()).containsOnly(roomPriceBack);
        assertThat(roomPriceBack.getRoom()).isEqualTo(room);

        room.removeRoomPrice(roomPriceBack);
        assertThat(room.getRoomPrices()).doesNotContain(roomPriceBack);
        assertThat(roomPriceBack.getRoom()).isNull();

        room.roomPrices(new HashSet<>(Set.of(roomPriceBack)));
        assertThat(room.getRoomPrices()).containsOnly(roomPriceBack);
        assertThat(roomPriceBack.getRoom()).isEqualTo(room);

        room.setRoomPrices(new HashSet<>());
        assertThat(room.getRoomPrices()).doesNotContain(roomPriceBack);
        assertThat(roomPriceBack.getRoom()).isNull();
    }

    @Test
    void imagesTest() {
        Room room = getRoomRandomSampleGenerator();
        Image imageBack = getImageRandomSampleGenerator();

        room.addImages(imageBack);
        assertThat(room.getImages()).containsOnly(imageBack);
        assertThat(imageBack.getRoom()).isEqualTo(room);

        room.removeImages(imageBack);
        assertThat(room.getImages()).doesNotContain(imageBack);
        assertThat(imageBack.getRoom()).isNull();

        room.images(new HashSet<>(Set.of(imageBack)));
        assertThat(room.getImages()).containsOnly(imageBack);
        assertThat(imageBack.getRoom()).isEqualTo(room);

        room.setImages(new HashSet<>());
        assertThat(room.getImages()).doesNotContain(imageBack);
        assertThat(imageBack.getRoom()).isNull();
    }

    @Test
    void hotelTest() {
        Room room = getRoomRandomSampleGenerator();
        Hotel hotelBack = getHotelRandomSampleGenerator();

        room.setHotel(hotelBack);
        assertThat(room.getHotel()).isEqualTo(hotelBack);

        room.hotel(null);
        assertThat(room.getHotel()).isNull();
    }
}
