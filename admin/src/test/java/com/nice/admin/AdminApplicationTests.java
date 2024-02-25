package com.nice.admin;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AdminApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminApplicationTests {
    private static final String localhost = "http://localhost:";

    private static final RestTemplate restTemplate = new RestTemplate();

//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @AfterAll
//    public void tearDown(){
//        SpringApplication.exit(applicationContext);
//    }

    @Test
    void healthByZipkinTest() {
        String res = restTemplate.getForObject(localhost + "9411/zipkin", String.class);
        assertThat(res).isNotNull();
    }

}
