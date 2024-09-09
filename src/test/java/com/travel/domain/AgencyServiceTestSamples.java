package com.travel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgencyServiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AgencyService getAgencyServiceSample1() {
        return new AgencyService().id(1L).title("title1").icon("icon1").content("content1");
    }

    public static AgencyService getAgencyServiceSample2() {
        return new AgencyService().id(2L).title("title2").icon("icon2").content("content2");
    }

    public static AgencyService getAgencyServiceRandomSampleGenerator() {
        return new AgencyService()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString());
    }
}
