package com.travel.domain;

import static com.travel.domain.CustomerTestSamples.*;
import static com.travel.domain.LoyaltyTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoyaltyTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoyaltyTransaction.class);
        LoyaltyTransaction loyaltyTransaction1 = getLoyaltyTransactionSample1();
        LoyaltyTransaction loyaltyTransaction2 = new LoyaltyTransaction();
        assertThat(loyaltyTransaction1).isNotEqualTo(loyaltyTransaction2);

        loyaltyTransaction2.setId(loyaltyTransaction1.getId());
        assertThat(loyaltyTransaction1).isEqualTo(loyaltyTransaction2);

        loyaltyTransaction2 = getLoyaltyTransactionSample2();
        assertThat(loyaltyTransaction1).isNotEqualTo(loyaltyTransaction2);
    }

    @Test
    void customerTest() {
        LoyaltyTransaction loyaltyTransaction = getLoyaltyTransactionRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        loyaltyTransaction.setCustomer(customerBack);
        assertThat(loyaltyTransaction.getCustomer()).isEqualTo(customerBack);

        loyaltyTransaction.customer(null);
        assertThat(loyaltyTransaction.getCustomer()).isNull();
    }
}
