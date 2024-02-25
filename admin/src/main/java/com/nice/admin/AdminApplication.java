package com.nice.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAdminServer
@SpringBootApplication
public class AdminApplication {
    //http://localhost:8080/applications
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
