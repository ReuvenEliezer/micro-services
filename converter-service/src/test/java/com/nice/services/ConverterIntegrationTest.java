package com.nice.services;

import com.nice.utils.WsAddressConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("integration-tests")
@Disabled
@SpringBootTest
class ConverterIntegrationTest {
    private static final String localhost = "http://localhost:";
    private static final String stringType = "string";
    private static final String hexType = "hex";
    private static final String fractionType = "fraction";

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

    @Test
    void healthByActuatorTest() {
        String res = restTemplate.getForObject(localhost + serverPort + "/actuator/health", String.class);
        assertThat(res).isEqualTo("{\"status\":\"UP\"}");
    }

    @ParameterizedTest()
    @MethodSource({"convertArgumentsProvider"})
    void convertTest(String input, String convertType, Integer expected) throws InterruptedException {
        BigDecimal bigDecimal = restTemplate.postForObject(localhost + serverPort + WsAddressConstants.convertLogicUrl + convertType, input, BigDecimal.class);
        Assertions.assertEquals(expected.intValue(), bigDecimal.intValue());
        sleep(7000);
        // TODO check in the output of aggregation service - the accumulation value by reading writer type
    }


    @ParameterizedTest()
    @MethodSource({"negativeArgumentsProvider"})
    void negativeTest(String input, String convertType) {
        Assertions.assertThrows(HttpServerErrorException.InternalServerError.class, () ->
                restTemplate.postForObject(localhost + serverPort + WsAddressConstants.convertLogicUrl + convertType, input, BigDecimal.class));
    }

    private static Stream<Arguments> convertArgumentsProvider() {
        return Stream.of(
                Arguments.of("F", hexType, 15),
                Arguments.of("abc", stringType, 294),
                Arguments.of("6/3", fractionType, 2)
        );
    }

    private static Stream<Arguments> negativeArgumentsProvider() {
        return Stream.of(
                Arguments.of("-3/6", fractionType),
                Arguments.of("0/1", fractionType),
                Arguments.of("3/0", fractionType),
                Arguments.of("$", hexType),
                Arguments.of("3", stringType),
                Arguments.of("a", fractionType),
                Arguments.of("12/1/1", fractionType)
        );
    }


}
