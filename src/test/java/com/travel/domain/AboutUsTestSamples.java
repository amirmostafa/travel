package com.travel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AboutUsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AboutUs getAboutUsSample1() {
        return new AboutUs().id(1L).description("description1").contactDetails("contactDetails1").additionalInfo("additionalInfo1");
    }

    public static AboutUs getAboutUsSample2() {
        return new AboutUs().id(2L).description("description2").contactDetails("contactDetails2").additionalInfo("additionalInfo2");
    }

    public static AboutUs getAboutUsRandomSampleGenerator() {
        return new AboutUs()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .contactDetails(UUID.randomUUID().toString())
            .additionalInfo(UUID.randomUUID().toString());
    }
}
