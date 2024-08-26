package com.travel.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RoomPriceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RoomPrice getRoomPriceSample1() {
        return new RoomPrice().id(1L);
    }

    public static RoomPrice getRoomPriceSample2() {
        return new RoomPrice().id(2L);
    }

    public static RoomPrice getRoomPriceRandomSampleGenerator() {
        return new RoomPrice().id(longCount.incrementAndGet());
    }
}
