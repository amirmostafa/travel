package com.travel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TourPackageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TourPackage getTourPackageSample1() {
        return new TourPackage().id(1L).name("name1").description("description1").durationDays(1);
    }

    public static TourPackage getTourPackageSample2() {
        return new TourPackage().id(2L).name("name2").description("description2").durationDays(2);
    }

    public static TourPackage getTourPackageRandomSampleGenerator() {
        return new TourPackage()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .durationDays(intCount.incrementAndGet());
    }
}
