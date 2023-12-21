package com.nice.services;

import com.nice.ConverterApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ConverterApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QueueTest {

    @Autowired
    private QueueService queueService;

    @Test
    void test(){
        queueService.put(new BigDecimal(1));
//        Assert.assertEquals();
    }


}
