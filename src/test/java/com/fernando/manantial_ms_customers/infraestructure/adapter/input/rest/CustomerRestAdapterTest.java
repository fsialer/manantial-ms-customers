package com.fernando.manantial_ms_customers.infraestructure.adapter.input.rest;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.application.ports.input.GetMetricsUseCase;
import com.fernando.manantial_ms_customers.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.domain.models.Metric;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.CustomerRestAdapter;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.MetricResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @MockitoBean
    private SaveCustomerUseCase saveCustomerUseCase;

    @MockitoBean
    private GetMetricsUseCase getMetricsUseCase;


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

    @Test
    @DisplayName("When Information Ingress Customer Is Correct Expect Information Customer Saved Correctly")
    void When_InformationIngressCustomerIsCorrect_Expect_InformationCustomerSavedWithResponse201(){
        CustomerRequest customerRequest=TestUtilCustomer.buildMockCustomerRequest();
        Customer customer = TestUtilCustomer.buildMockCustomer();
        CustomerResponse customerResponse= TestUtilCustomer.buildMockCustomerResponse();
        when(saveCustomerUseCase.save(any(Customer.class))).thenReturn(Mono.just(customer));
        when(customerRestMapper.customerRequestToCustomer(any(CustomerRequest.class))).thenReturn(customer);
        when(customerRestMapper.customerToCustomerResponse(any(Customer.class))).thenReturn(customerResponse);

        webTestClient.post()
                .uri("/v1/customers")
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(customerResponse.id())
                .jsonPath("$.name").isEqualTo(customerResponse.name())
                .jsonPath("$.lastName").isEqualTo(customerResponse.lastName())
                .jsonPath("$.age").isEqualTo(customerResponse.age())
                .jsonPath("$.birthDate").isEqualTo(customerResponse.birthDate());

        Mockito.verify(saveCustomerUseCase,times(1)).save(any(Customer.class));
        Mockito.verify(customerRestMapper,times(1)).customerRequestToCustomer(any(CustomerRequest.class));
        Mockito.verify(customerRestMapper,times(1)).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    @DisplayName("When Have Customers Available Expect Obtain Metric Of Average And Standard Deviation")
    void When_HaveCustomersAvailable_Expect_ObtainMetricOfAverageAndStandardDeviation(){
        MetricResponse metricResponse=TestUtilCustomer.buildMockMetricResponse();
        when(getMetricsUseCase.getMetrics()).thenReturn(Mono.just(TestUtilCustomer.buildMockMetric()));
        when(customerRestMapper.metricToMetricResponse(any(Metric.class))).thenReturn(metricResponse);

        webTestClient.get()
                .uri("/v1/customers/metrics")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.average").isEqualTo(metricResponse.average())
                .jsonPath("$.standardDeviation").isEqualTo(metricResponse.standardDeviation());

        Mockito.verify(getMetricsUseCase,times(1)).getMetrics();
        Mockito.verify(customerRestMapper,times(1)).metricToMetricResponse(any(Metric.class));
    }
}
