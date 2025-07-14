package com.fernando.manantial_ms_customers.domain.strategy;

import com.fernando.manantial_ms_customers.domain.exceptions.CustomerRuleException;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;

@Component
public class CompareAgeWithAgeOfBirthDate implements CustomerRule{
    @Override
    public Mono<Void> validateRule(Customer customer) {
        LocalDate birthDate = customer.getBirthDate();
        int ageCalculated = Period.between(birthDate, LocalDate.now()).getYears();

        if (ageCalculated != customer.getAge()) {
            return Mono.error(new CustomerRuleException("Age not equals with age of birthdate."));
        }
        return Mono.empty();
    }

    @Override
    public boolean isApplicable(String codeValidation) {
        return "RULE001".equals(codeValidation);
    }
}
