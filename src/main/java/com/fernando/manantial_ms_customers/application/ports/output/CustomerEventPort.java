package com.fernando.manantial_ms_customers.application.ports.output;

import com.fernando.manantial_ms_customers.domain.models.Customer;

public interface CustomerEventPort {
    void publishCustomerSaved(Customer customer);
}
