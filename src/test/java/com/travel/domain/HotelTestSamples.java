package com.travel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HotelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Hotel getHotelSample1() {
        return new Hotel().id(1L).name("name1").address("address1").starRating(1).contactNumber("contactNumber1").email("email1");
    }

    public static Hotel getHotelSample2() {
        return new Hotel().id(2L).name("name2").address("address2").starRating(2).contactNumber("contactNumber2").email("email2");
    }

    public static Hotel getHotelRandomSampleGenerator() {
        return new Hotel()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .starRating(intCount.incrementAndGet())
            .contactNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
