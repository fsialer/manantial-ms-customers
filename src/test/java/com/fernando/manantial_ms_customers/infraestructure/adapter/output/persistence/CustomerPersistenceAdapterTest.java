package com.fernando.manantial_ms_customers.infraestructure.adapter.output.persistence;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.CustomerPersistenceAdapter;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.repositories.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerPersistenceAdapterTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerPersistenceMapper customerPersistenceMapper;

    @InjectMocks
    private CustomerPersistenceAdapter customerPersistenceAdapter;


    @Test
    @DisplayName("When Exist Information Customers Expect A List Customers")
    void When_ExistInformationCustomers_Expect_AListCustomers(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        Customer customer2 = TestUtilCustomer.buildMockCustomer2();

        CustomerDocument customerDocument1 = TestUtilCustomer.buildMockCustomerDocument();
        CustomerDocument customerDocument2 = TestUtilCustomer.buildMockCustomerDocument2();
        when(customerPersistenceMapper.customerDocumenFluxtoToCustomerFlux(any(Flux.class))).thenReturn(Flux.just(customer1,customer2));
        when(customerRepository.findAll()).thenReturn(Flux.just(customerDocument1,customerDocument2));

        Flux<Customer> customerFlux=customerPersistenceAdapter.getCustomers();

        StepVerifier.create(customerFlux)
                .consumeNextWith(customer -> {
                    assertEquals(customer.getId(),customer1.getId());
                    assertEquals(customer.getName(),customer1.getName());
                    assertEquals(customer.getLastName(),customer1.getLastName());
                    assertEquals(customer.getAge(),customer1.getAge());
                    assertEquals(customer.getBirthDate(),customer1.getBirthDate());
                })
                .consumeNextWith(customer -> {
                    assertEquals(customer.getId(),customer2.getId());
                    assertEquals(customer.getName(),customer2.getName());
                    assertEquals(customer.getLastName(),customer2.getLastName());
                    assertEquals(customer.getAge(),customer2.getAge());
                    assertEquals(customer.getBirthDate(),customer2.getBirthDate());
                })
                .verifyComplete();
        Mockito.verify(customerPersistenceMapper,times(1)).customerDocumenFluxtoToCustomerFlux(any(Flux.class));
        Mockito.verify(customerRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("When Information Customer Is Correct Expect Customer save correctly")
    void When_InformationCustomerIsCorrect_Expect_CustomerSaveCorrectly(){
        CustomerDocument customerDocument = TestUtilCustomer.buildMockCustomerDocument();
        Customer customer = TestUtilCustomer.buildMockCustomer();
        when(customerRepository.save(any(CustomerDocument.class))).thenReturn(Mono.just(customerDocument));
        when(customerPersistenceMapper.customerToCustomerDocument(any(Customer.class))).thenReturn(customerDocument);
        when(customerPersistenceMapper.customerDocumentMonoToCustomerMono(any(Mono.class))).thenReturn(Mono.just(customer));

        Mono<Customer> customerMono=customerPersistenceAdapter.saveCustomer(customer);
        StepVerifier.create(customerMono)
                .expectNextMatches(customerMatch->
                    customerMatch.getId().equals(customer.getId())
                            && customerMatch.getName().equals(customer.getName())
                            && customerMatch.getLastName().equals(customer.getLastName())
                            && customerMatch.getAge().equals(customer.getAge())
                            && customerMatch.getBirthDate().equals(customer.getBirthDate())
                ).verifyComplete();
        Mockito.verify(customerRepository,times(1)).save(any(CustomerDocument.class));
        Mockito.verify(customerPersistenceMapper,times(1)).customerDocumentMonoToCustomerMono(any(Mono.class));
        Mockito.verify(customerPersistenceMapper,times(1)).customerToCustomerDocument(any(Customer.class));
    }


}
