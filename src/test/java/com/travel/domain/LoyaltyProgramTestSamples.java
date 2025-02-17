package com.travel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoyaltyProgramTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LoyaltyProgram getLoyaltyProgramSample1() {
        return new LoyaltyProgram().id(1L).name("name1").description("description1").pointsPerDollar(1).rewardThreshold(1);
    }

    public static LoyaltyProgram getLoyaltyProgramSample2() {
        return new LoyaltyProgram().id(2L).name("name2").description("description2").pointsPerDollar(2).rewardThreshold(2);
    }

    public static LoyaltyProgram getLoyaltyProgramRandomSampleGenerator() {
        return new LoyaltyProgram()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .pointsPerDollar(intCount.incrementAndGet())
            .rewardThreshold(intCount.incrementAndGet());
    }
}
