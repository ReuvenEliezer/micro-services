package com.nice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.nice.config",
        "com.nice.controllers",
        "com.nice.services",
})
@EnableDiscoveryClient
public class AggregationApp {
    public static void main(String[] args) {
        SpringApplication.run(AggregationApp.class, args);
    }
}
