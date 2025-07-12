package com.fernando.manantial_ms_customers.infraestructure.adapter.input.rest;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.CustomerRestAdapter;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(CustomerRestAdapter.class)
class CustomerRestAdapterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetCustomersUseCase getCustomersUseCase;
    
    @MockitoBean
    private CustomerRestMapper customerRestMapper;


    @Test
    @DisplayName("When_RequestInformationCustomer_Expect_AListCustomersAvailable")
    void When_RequestInformationCustomer_Expect_AListCustomersAvailable(){
        CustomerResponse customerResponse1= TestUtilCustomer.buildMockCustomerResponse();
        CustomerResponse customerResponse2= TestUtilCustomer.buildMockCustomerResponse2();
        Customer customer1= TestUtilCustomer.buildMockCustomer();
        Customer customer2= TestUtilCustomer.buildMockCustomer2();

        when(getCustomersUseCase.getCustomers()).thenReturn(Flux.just(customer1,customer2));
        when(customerRestMapper.customerFluxToCustomerResponseFlux(any(Flux.class))).thenReturn(Flux.just(customerResponse1,customerResponse2));
        
        webTestClient.get()
                .uri("/v1/customers")
                .exchange()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(customerResponse1.id())
                .jsonPath("$[0].name").isEqualTo(customerResponse1.name())
                .jsonPath("$[0].lastName").isEqualTo(customerResponse1.lastName())
                .jsonPath("$[0].age").isEqualTo(customerResponse1.age())
                .jsonPath("$[0].birthDate").isEqualTo(customerResponse1.birthDate())
                .jsonPath("$[1].id").isEqualTo(customerResponse2.id())
                .jsonPath("$[1].name").isEqualTo(customerResponse2.name())
                .jsonPath("$[1].lastName").isEqualTo(customerResponse2.lastName())
                .jsonPath("$[1].age").isEqualTo(customerResponse2.age())
                .jsonPath("$[1].birthDate").isEqualTo(customerResponse2.birthDate());

        Mockito.verify(getCustomersUseCase,times(1)).getCustomers();
        Mockito.verify(customerRestMapper,times(1)).customerFluxToCustomerResponseFlux(any(Flux.class));
    }
}
