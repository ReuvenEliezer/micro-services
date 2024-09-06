package com.nice.services;

import com.nice.ConverterApp;
import com.nice.utils.WsAddressConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

//@ActiveProfiles(profiles = "integration-tests") //https://stackoverflow.com/questions/44055969/in-spring-what-is-the-difference-between-profile-and-activeprofiles
@EnabledIf(value = "#{environment.getActiveProfiles()[0] == 'integration-tests'}", loadContext = true)
//@Disabled
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConverterIntegrationTest {

    private static final Logger logger = LogManager.getLogger(ConverterIntegrationTest.class);

    private static final String localhost = "http://localhost:";
    private static final String stringType = "string";
    private static final String hexType = "hex";
    private static final String fractionType = "fraction";

    @Autowired
    private RestClient restClient;

    @Autowired
    private Environment environment;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
//    @LocalServerPort
    private int serverPort;

    @Value("${aggregation.server.port}")
    private int aggServerPort;

    @Test
    @Disabled
    void zipkinTest() {
        String[] res = restClient.get().uri(localhost + "9411/zipkin/api/v2/services").retrieve().body(String[].class);
        logger.info("zipkin services: '{}'", Arrays.toString(res));
        assertThat(res).isNotEmpty();
        assertThat(res).containsExactlyInAnyOrder(
//                appName,
                "aggregation-service");
    }

    @Test
    void callAggregateServiceTest() {
        logger.info("callAggregateServiceTest");
        String[] activeProfiles = environment.getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            logger.info("activeProfile: " + activeProfile);
        }
        BigDecimal result = restClient
                .get()
//                .uri(localhost + serverPort + WsAddressConstants.convertLogicUrl + "call-aggregate-service")
                .uri(localhost + "8080" + WsAddressConstants.convertLogicUrl + "call-aggregate-service") // call via gateway
                .retrieve()
                .body(BigDecimal.class);
        assertThat(result).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }


    @Test
    void callAggregateServiceWithValueTest() {
        logger.info("callAggregateServiceWithValueTest");
        String[] activeProfiles = environment.getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            logger.info("activeProfile: " + activeProfile);
        }

        int value = 5;
        restClient
                .get()
                .uri(localhost + "8080/aggregate/" + value)  // call via gateway
//                .uri(localhost + aggServerPort + "/aggregate/" + value)
                .retrieve()
                .body(Void.class);

        BigDecimal result = restClient
                .get()
//                .uri(localhost + serverPort + WsAddressConstants.convertLogicUrl + "call-aggregate-service")
                .uri(localhost + "8080" + WsAddressConstants.convertLogicUrl + "call-aggregate-service")  // call via gateway
                .retrieve()
                .body(BigDecimal.class);
        assertThat(result).isGreaterThanOrEqualTo(BigDecimal.valueOf(value));
    }

    @Test
    void healthByActuatorTest() {
        String res = restClient.get().uri(localhost + serverPort + "/actuator/health").retrieve().body(String.class);
        assertThat(res).isEqualTo("{\"status\":\"UP\"}");
    }

    @ParameterizedTest()
    @MethodSource({"convertArgumentsProvider"})
    void convertTest(String input, String convertType, int expected) {
        BigDecimal bigDecimal = restClient
                .post()
                .uri(localhost + serverPort + WsAddressConstants.convertLogicUrl + convertType)
                .body(input)
                .retrieve()
                .body(BigDecimal.class);
        Assertions.assertEquals(expected, bigDecimal.intValue());
//        sleep(7000);
        // TODO check in the output of aggregation service - the accumulation value by reading writer type
    }


    @ParameterizedTest()
    @MethodSource({"negativeArgumentsProvider"})
    void negativeTest(String input, String convertType) {
        Assertions.assertThrows(HttpServerErrorException.InternalServerError.class, () ->
                restClient.post()
                        .uri(localhost + serverPort + WsAddressConstants.convertLogicUrl + convertType)
                        .body(input)
                        .retrieve()
                        .body(BigDecimal.class));
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
