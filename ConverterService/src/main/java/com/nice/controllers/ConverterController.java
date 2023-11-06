package com.nice.controllers;

import com.nice.services.ConverterService;
import com.nice.services.ConverterServiceImpl;
import com.nice.utils.WsAddressConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
@RequestMapping(WsAddressConstants.convertLogicUrl)
public class ConverterController {

    private static final Logger logger = LogManager.getLogger(ConverterController.class);

    private static final String localhost = "http://localhost:";

    @Autowired
    private ConverterService converterService;

    @Autowired
    private RestTemplate restTemplate;


    @PostMapping(value = "string")
    public BigDecimal convertStr(@RequestBody String str) {
        return converterService.convertStr(str);
    }

    @PostMapping(value = "hex")
    public BigDecimal convertHex(@RequestBody String hex) {
        return converterService.convertHex(hex);
    }

    @PostMapping(value = "fraction")
    public BigDecimal convertFraction(@RequestBody String fraction) {
        return converterService.convertFraction(fraction);
    }

    @GetMapping(value = "call-aggregate-service")
    public BigDecimal getAggregateValue() {
        logger.info("call-aggregate-service: getAggregateValue");
        return restTemplate.getForObject(localhost + 8081 + "/aggregate/get-aggregate-value" , BigDecimal.class);
    }

}
