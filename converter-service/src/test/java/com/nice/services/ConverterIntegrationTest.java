package com.nice.services;

import com.nice.ConverterApp;
import com.nice.utils.WsAddressConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

//@ActiveProfiles("integration-test")
//@Disabled

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ConverterApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConverterIntegrationTest {
    private static final String localhost = "http://localhost:";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private Integer serverPort;

    @Test
    void callAggregateServiceTest() {
        BigDecimal forObject = restTemplate.getForObject(localhost + serverPort + WsAddressConstants.convertLogicUrl + "call-aggregate-service", BigDecimal.class);
        Assertions.assertEquals(BigDecimal.ZERO, forObject);
    }


}
