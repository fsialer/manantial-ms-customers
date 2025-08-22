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
import static org.mockito.ArgumentMatchers.*;
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

    @Test
    @DisplayName("When Customer Id Exists Expect Information Customer")
    void When_CustomerIdExists_Expect_InformationCustomer(){
        CustomerDocument customerDocument = TestUtilCustomer.buildMockCustomerDocument();
        Customer customer=TestUtilCustomer.buildMockCustomer();
        when(customerRepository.findById(anyString())).thenReturn(Mono.just(customerDocument));
        when(customerPersistenceMapper.customerDocumentMonoToCustomerMono(any())).thenReturn(Mono.just(customer));

        Mono<Customer> customerInfo=customerPersistenceAdapter.getCustomer("1");

        StepVerifier.create(customerInfo)
                .expectNextMatches(customerMatch->
                        customerMatch.getId().equals(customer.getId())
                                && customerMatch.getName().equals(customer.getName())
                                && customerMatch.getLastName().equals(customer.getLastName())
                                && customerMatch.getAge().equals(customer.getAge())
                                && customerMatch.getBirthDate().equals(customer.getBirthDate())
                ).verifyComplete();
        Mockito.verify(customerPersistenceMapper,times(1)).customerDocumentMonoToCustomerMono(any());
        Mockito.verify(customerRepository,times(1)).findById(anyString());

    }

    @Test
    @DisplayName("When CustomerId Exists Expect Delete Customer By Id")
    void When_CustomerIdExists_Expect_DeleteCustomerById(){
        when(customerRepository.deleteById(anyString())).thenReturn(Mono.empty());
        Mono<Void> delete=customerPersistenceAdapter.deleteCustomer("1");
        StepVerifier.create(delete)
                .verifyComplete();
        Mockito.verify(customerRepository,times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("When Customers Search By Page And Size Expect A List Customers Paginated")
    void When_CustomersSearchByPageAndSize_Expect_AListCustomersPaginated(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        Customer customer2 = TestUtilCustomer.buildMockCustomer2();

        CustomerDocument customerDocument1 = TestUtilCustomer.buildMockCustomerDocument();
        CustomerDocument customerDocument2 = TestUtilCustomer.buildMockCustomerDocument2();
        when(customerPersistenceMapper.customerDocumenFluxtoToCustomerFlux(any(Flux.class))).thenReturn(Flux.just(customer1,customer2));
        when(customerRepository.getCustomerPaginated(anyInt(),anyInt())).thenReturn(Flux.just(customerDocument1,customerDocument2));

        Flux<Customer> customerFlux=customerPersistenceAdapter.getCustomersPaged(1,2);

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
        Mockito.verify(customerRepository,times(1)).getCustomerPaginated(anyInt(),anyInt());
    }

    @Test
    @DisplayName("When Customer Have Data Availability Expect A Quantity")
    void When_CustomerHaveDataAvailability_Expect_AQuantity(){
        when(customerRepository.count()).thenReturn(Mono.just(2L));
        Mono<Long> quantity = customerPersistenceAdapter.count();
        StepVerifier.create(quantity)
                .consumeNextWith(quantity2->{
                    assertEquals(2L, quantity2);
                })
                .verifyComplete();
        Mockito.verify(customerRepository,times(1)).count();
    }


}
