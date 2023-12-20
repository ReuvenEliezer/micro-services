package com.nice.services;

import com.nice.AggregationApp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = AggregationApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AggregationTest {

    private static final Logger logger = LogManager.getLogger(AggregationTest.class);

    @Test
    void test(){
//        Assertions.fail("remove test");
        logger.info("test");
    }
}
