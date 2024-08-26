package com.travel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomPriceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomPriceDTO.class);
        RoomPriceDTO roomPriceDTO1 = new RoomPriceDTO();
        roomPriceDTO1.setId(1L);
        RoomPriceDTO roomPriceDTO2 = new RoomPriceDTO();
        assertThat(roomPriceDTO1).isNotEqualTo(roomPriceDTO2);
        roomPriceDTO2.setId(roomPriceDTO1.getId());
        assertThat(roomPriceDTO1).isEqualTo(roomPriceDTO2);
        roomPriceDTO2.setId(2L);
        assertThat(roomPriceDTO1).isNotEqualTo(roomPriceDTO2);
        roomPriceDTO1.setId(null);
        assertThat(roomPriceDTO1).isNotEqualTo(roomPriceDTO2);
    }
}
