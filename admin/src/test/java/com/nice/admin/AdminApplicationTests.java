package com.nice.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = AdminApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminApplicationTests {
    private static final String localhost = "http://localhost:";

    private static final RestTemplate restTemplate = new RestTemplate();


    @Test
    void healthByZipkinTest() {
        String res = restTemplate.getForObject(localhost + "9411/zipkin", String.class);
        assertThat(res).isNotNull();
    }
}
