package com.nice.services;

import com.nice.ConverterApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ConverterApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConverterTest {

    private static final String localhost = "http://localhost:";

    @Autowired
    private QueueService queueService;

    @Autowired
    private RestClient restClient;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${spring.boot.admin.client.instance.service-base-url}")
    private String bootAdminUrl;

    @Test
    void healthByZipkinTest() {
        String res = restClient.get().uri(localhost + "9411/zipkin").retrieve().body(String.class);
        assertThat(res).isNotNull();
    }
    @Test
    void bootAdminUrlTest() {
        assertThat(bootAdminUrl).isEqualTo("http://localhost:" + serverPort);
    }

    @Test
    void test(){
        queueService.put(new BigDecimal(1));
//        Assert.assertEquals();
    }


}
