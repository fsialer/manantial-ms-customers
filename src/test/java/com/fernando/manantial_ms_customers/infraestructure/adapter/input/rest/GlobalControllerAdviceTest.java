package com.fernando.manantial_ms_customers.infraestructure.adapter.input.rest;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_customers.domain.exceptions.CustomerRuleException;
import com.fernando.manantial_ms_customers.domain.exceptions.RuleStrategyException;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.CustomerRestAdapter;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.fernando.manantial_ms_customers.infrastructure.utils.ErrorCatalog.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(CustomerRestAdapter.class)
class GlobalControllerAdviceTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CustomerRestMapper customerRestMapper;

    @MockitoBean
    private SaveCustomerUseCase saveCustomerUseCase;

    @MockitoBean
    private GetCustomersUseCase getCustomersUseCase;

    @Test
    @DisplayName("Expect WebExchangeBindException When Name Customer Is Not Defined")
    void Expect_WebExchangeBindException_When_NameCustomerIsNotDefined(){
        CustomerRequest customerRequest= TestUtilCustomer.buildMockCustomerRequest();
        customerRequest.setName(null);
        Customer customer = TestUtilCustomer.buildMockCustomer();
        CustomerResponse customerResponse= TestUtilCustomer.buildMockCustomerResponse();
        when(saveCustomerUseCase.save(any(Customer.class))).thenReturn(Mono.just(customer));
        when(customerRestMapper.customerRequestToCustomer(any(CustomerRequest.class))).thenReturn(customer);
        when(customerRestMapper.customerToCustomerResponse(any(Customer.class))).thenReturn(customerResponse);

        webTestClient.post()
                .uri("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response->{
                    assertEquals(response.code(),CUSTOMER_BAD_PARAMETER.getCode());
                    assertEquals(response.message(),CUSTOMER_BAD_PARAMETER.getMessage());

                });

        Mockito.verify(saveCustomerUseCase,times(0)).save(any(Customer.class));
        Mockito.verify(customerRestMapper,times(0)).customerRequestToCustomer(any(CustomerRequest.class));
        Mockito.verify(customerRestMapper,times(0)).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    @DisplayName("Expect CustomerRuleException When Customer Rules Are Not Valid")
    void Expect_CustomerRuleException_When_CustomerRulesAreNotValid(){
        CustomerRequest customerRequest= TestUtilCustomer.buildMockCustomerRequest();
        customerRequest.setAge(36);
        Customer customer = TestUtilCustomer.buildMockCustomer();
        CustomerResponse customerResponse= TestUtilCustomer.buildMockCustomerResponse();
        when(saveCustomerUseCase.save(any(Customer.class))).thenReturn(Mono.error(new CustomerRuleException("Age not equals with age of birthdate.")));
        when(customerRestMapper.customerRequestToCustomer(any(CustomerRequest.class))).thenReturn(customer);
        when(customerRestMapper.customerToCustomerResponse(any(Customer.class))).thenReturn(customerResponse);

        webTestClient.post()
                .uri("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response->{
                    assertEquals(response.code(),CUSTOMER_RULE.getCode());
                    assertEquals(response.message(),CUSTOMER_RULE.getMessage());
                });

        Mockito.verify(saveCustomerUseCase,times(1)).save(any(Customer.class));
        Mockito.verify(customerRestMapper,times(1)).customerRequestToCustomer(any(CustomerRequest.class));
        Mockito.verify(customerRestMapper,times(0)).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    @DisplayName("Expect RuleStrategyException When Rule Code Do Not Exists")
    void Expect_RuleStrategyException_When_CustomerRulesAreNotValid(){
        CustomerRequest customerRequest= TestUtilCustomer.buildMockCustomerRequest();
        Customer customer = TestUtilCustomer.buildMockCustomer();
        CustomerResponse customerResponse= TestUtilCustomer.buildMockCustomerResponse();
        when(saveCustomerUseCase.save(any(Customer.class))).thenReturn(Mono.error(new RuleStrategyException("Do not Exist any rule applicable")));
        when(customerRestMapper.customerRequestToCustomer(any(CustomerRequest.class))).thenReturn(customer);
        when(customerRestMapper.customerToCustomerResponse(any(Customer.class))).thenReturn(customerResponse);

        webTestClient.post()
                .uri("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response->{
                    assertEquals(response.code(),CUSTOMER_RULE_STRATEGY.getCode());
                    assertEquals(response.message(),CUSTOMER_RULE_STRATEGY.getMessage());
                });

        Mockito.verify(saveCustomerUseCase,times(1)).save(any(Customer.class));
        Mockito.verify(customerRestMapper,times(1)).customerRequestToCustomer(any(CustomerRequest.class));
        Mockito.verify(customerRestMapper,times(0)).customerToCustomerResponse(any(Customer.class));
    }

}
