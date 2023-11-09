package com.nice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.nice.config",
        "com.nice.controllers",
        "com.nice.services",
})
public class ConverterApp {
    public static void main(String[] args) {
        SpringApplication.run(ConverterApp.class, args);
    }
}
