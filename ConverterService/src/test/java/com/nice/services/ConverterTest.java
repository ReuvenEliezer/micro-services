package com.nice.services;

import com.nice.ConverterApp;
import com.nice.utils.WsAddressConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ConverterApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ConverterTest {

    private static final String localhost = "http://localhost:";
    private static final String stringType = "string";
    private static final String hexType = "hex";
    private static final String fractionType = "fraction";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private Integer serverPort;


    @ParameterizedTest()
    @MethodSource({"convertArgumentsProvider"})
    public void convertTest(String input, String convertType, Integer expected) throws InterruptedException {
        BigDecimal bigDecimal = restTemplate.postForObject(localhost + serverPort + WsAddressConstants.convertLogicUrl + convertType, input, BigDecimal.class);
        Assertions.assertEquals(expected.intValue(), bigDecimal.intValue());
        Thread.sleep(7000);
        // TODO check in the output of aggregation service - the accumulation value by reading writer type
    }


    @ParameterizedTest()
    @MethodSource({"negativeArgumentsProvider"})
    public void negativeTest(String input, String convertType) {
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
