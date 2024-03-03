package com.nice.services;

import com.nice.ConverterApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ConverterApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConverterTest {

    @Autowired
    private QueueService queueService;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${spring.boot.admin.client.instance.service-base-url}")
    private String bootAdminClientUrl;

    @Value("${spring.application.name}")
    private String serviceName;


    @Test
    void bootAdminUrlTest() {
        assertThat(bootAdminClientUrl).isEqualTo("http://" + serviceName +":" + serverPort);
    }

    @Test
    void test() {
        queueService.put(new BigDecimal(1));
//        Assert.assertEquals();
    }


}
