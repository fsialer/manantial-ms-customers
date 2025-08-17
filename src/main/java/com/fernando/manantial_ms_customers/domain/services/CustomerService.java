package com.fernando.manantial_ms_customers.domain.services;

import com.fernando.manantial_ms_customers.application.ports.input.*;
import com.fernando.manantial_ms_customers.application.ports.output.CalculateMetricsPort;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerEventPort;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_customers.domain.exceptions.CustomerNotFoundException;
import com.fernando.manantial_ms_customers.domain.exceptions.RuleStrategyException;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.domain.models.Metric;
import com.fernando.manantial_ms_customers.domain.strategy.CustomerRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements GetCustomersUseCase, SaveCustomerUseCase, GetMetricsUseCase, UpdateCustomerUseCase, DeleteCustomerUseCase, GetCustomerUseCase {

    private final CustomerPersistencePort customerPersistencePort;
    private final List<CustomerRule> listCustomerRule;
    private final CalculateMetricsPort calculateMetricsPort;
    private final CustomerEventPort customerEventPort;

    @Override
    public Flux<Customer> getCustomers() {
        return customerPersistencePort.getCustomers().flatMap(customer->{
            customer.setLifeExpectancy(customer.getBirthDate().plusYears(76));
            return Flux.just(customer);
        });
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        List<String> listCodeRule=List.of("RULE001");
        List<CustomerRule> rulesApplicable = listCustomerRule.stream()
                .filter(rule -> listCodeRule.stream().anyMatch(rule::isApplicable))
                .toList();

        if(rulesApplicable.isEmpty()){
            return Mono.error(new RuleStrategyException("Do not Exist any rule applicable"));
        }

        return Flux.fromIterable(rulesApplicable)
                .concatMap(rule -> rule.validateRule(customer))
                .then(customerPersistencePort.saveCustomer(customer)
                        .doOnSuccess(customerEventPort::publishCustomerSaved))
                .doOnError(e->log.error("Error: {}",e.getMessage()));
    }

    @Override
    public Mono<Metric> getMetrics() {
        return customerPersistencePort.getCustomers()
                .map(Customer::getAge)
                .collectList()
                .flatMap(ages->{
                    Double average=calculateMetricsPort.calculateAverage(ages);
                    Double standardDeviation = calculateMetricsPort.calculateStandardDeviation(ages,average);
                    return Mono.just(Metric.builder().average(average).standardDeviation(standardDeviation).build());
                });
    }

    @Override
    public Mono<Customer> update(String id, Customer customer) {
        List<String> listCodeRule=List.of("RULE001");
        List<CustomerRule> rulesApplicable = listCustomerRule.stream()
                .filter(rule -> listCodeRule.stream().anyMatch(rule::isApplicable))
                .toList();

        if(rulesApplicable.isEmpty()){
            return Mono.error(new RuleStrategyException("Do not Exist any rule applicable"));
        }
        return Flux.fromIterable(rulesApplicable)
                .concatMap(rule -> rule.validateRule(customer))
                .then(customerPersistencePort.getCustomer(id)
                        .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found: ".concat(id))))
                        .flatMap(customer1->{
                            customer1.setName(customer.getName());
                            customer1.setLastName(customer.getLastName());
                            customer1.setAge(customer.getAge());
                            customer1.setBirthDate(customer.getBirthDate());
                                    return customerPersistencePort.saveCustomer(customer1)
                                            .doOnSuccess(customerEventPort::publishCustomerSaved);
                                }
                                )).doOnError(e->log.error("Error: {}",e.getMessage()));
    }

    @Override
    public Mono<Void> delete(String id) {
        return customerPersistencePort.getCustomer(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found: ".concat(id))))
                .flatMap(customer->customerPersistencePort.deleteCustomer(id))
                .doOnSuccess(unused -> customerEventPort.publishCustomerDeleted(id))
                .then();
    }

    @Override
    public Mono<Customer> getCustomer(String id) {
        return customerPersistencePort.getCustomer(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found: ".concat(id))));
    }
}
