package com.fernando.manantial_ms_customers.domain.strategy;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.domain.exceptions.CustomerRuleException;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CompareAgeWithAgeOfBirhDateTest {
    @Test
    @DisplayName("When AgeCustomer Is Correct Expect Void")
    void When_AgeCustomerIsCorrect_Expect_Void(){
        CompareAgeWithAgeOfBirthDate compareAgeWithAgeOfBirthDate=new CompareAgeWithAgeOfBirthDate();
        Customer customer= TestUtilCustomer.buildMockCustomer();
        StepVerifier.create(compareAgeWithAgeOfBirthDate.validateRule(customer))
                .verifyComplete();
    }

    @Test
    @DisplayName("Expect CustomerRuleException When Age Customer Is Not Correct")
    void Expect_CustomerRuleException_When_AgeCustomerIsNotCorrect(){
        CompareAgeWithAgeOfBirthDate compareAgeWithAgeOfBirthDate=new CompareAgeWithAgeOfBirthDate();
        Customer customer= TestUtilCustomer.buildMockCustomer();
        customer.setAge(36);
        StepVerifier.create(compareAgeWithAgeOfBirthDate.validateRule(customer))
                .expectError(CustomerRuleException.class)
                .verify();
    }

    @Test
    @DisplayName("When Code Rule Is Incorrect Expect False")
    void When_CodeRuleIsIncorrect_Expect_False(){
        CompareAgeWithAgeOfBirthDate compareAgeWithAgeOfBirthDate=new CompareAgeWithAgeOfBirthDate();
        assertFalse(compareAgeWithAgeOfBirthDate.isApplicable("RULE003"));
    }

    @Test
    @DisplayName("When Code Rule Is Correct Expect True")
    void When_CodeRuleIsCorrect_Expect_True(){
        CompareAgeWithAgeOfBirthDate compareAgeWithAgeOfBirthDate=new CompareAgeWithAgeOfBirthDate();
        assertTrue(compareAgeWithAgeOfBirthDate.isApplicable("RULE001"));
    }


}
