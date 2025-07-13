package com.fernando.manantial_ms_customers.domain.services;

import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.application.ports.input.GetMetricsUseCase;
import com.fernando.manantial_ms_customers.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_customers.application.ports.output.CalculateMetricsPort;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_customers.domain.exceptions.RuleStrategyException;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.domain.models.Metric;
import com.fernando.manantial_ms_customers.domain.strategy.CustomerRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService implements GetCustomersUseCase, SaveCustomerUseCase, GetMetricsUseCase {

    private final CustomerPersistencePort customerPersistencePort;
    private final List<CustomerRule> listCustomerRule;
    private final CalculateMetricsPort calculateMetricsPort;

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
                .then(customerPersistencePort.saveCustomer(customer));
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
}
