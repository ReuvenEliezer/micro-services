package com.reuven.controllers;


import com.reuven.utils.WsAddressConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@RestController
public class DiscoveryController {

    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;


    private final String appName;

    public DiscoveryController(DiscoveryClient discoveryClient,
                               RestClient.Builder restClientBuilder,
                               @Value("${spring.application.name}") String appName) {
        this.discoveryClient = discoveryClient;
        restClient = restClientBuilder.build();
        this.appName = appName;
    }

    @GetMapping("hello-eureka")
    public BigDecimal helloWorld() {
        ServiceInstance serviceInstance = discoveryClient.getInstances(appName).get(0);
        BigDecimal response = restClient.get()
                .uri(serviceInstance.getUri() + WsAddressConstants.aggregateLogicUrl + "get-aggregate-value")
                .retrieve()
                .body(BigDecimal.class);
        return response;
    }
}