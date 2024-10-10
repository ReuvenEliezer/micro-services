package com.reuven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.reuven.config",
        "com.reuven.controllers",
        "com.reuven.services",
})
@EnableDiscoveryClient
public class AggregationApp {
    public static void main(String[] args) {
        SpringApplication.run(AggregationApp.class, args);
    }
}
