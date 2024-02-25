package com.nice.services;

import com.nice.utils.WsAddressConstants;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("integration-tests")
@SpringBootTest
class ConverterIntegrationTest {
    private static final String localhost = "http://localhost:";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private Integer serverPort;

    @Test
//    @Disabled
    void callAggregateServiceTest() {
        BigDecimal forObject = restTemplate.getForObject(localhost + serverPort + WsAddressConstants.convertLogicUrl + "call-aggregate-service", BigDecimal.class);
        assertThat(forObject).isEqualTo(BigDecimal.ZERO);
    }

}
