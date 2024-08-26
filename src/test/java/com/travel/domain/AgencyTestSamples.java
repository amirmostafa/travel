package com.travel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgencyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Agency getAgencySample1() {
        return new Agency().id(1L).name("name1").address("address1").contactNumber("contactNumber1").email("email1").website("website1");
    }

    public static Agency getAgencySample2() {
        return new Agency().id(2L).name("name2").address("address2").contactNumber("contactNumber2").email("email2").website("website2");
    }

    public static Agency getAgencyRandomSampleGenerator() {
        return new Agency()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .contactNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString());
    }
}
