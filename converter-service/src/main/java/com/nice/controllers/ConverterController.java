package com.nice.controllers;

import com.nice.services.ConverterService;
import com.nice.utils.WsAddressConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
@RequestMapping(WsAddressConstants.convertLogicUrl)
public class ConverterController {

    private static final Logger logger = LogManager.getLogger(ConverterController.class);
    private static final String LOCAL_HOST = "http://localhost:";
    private final ConverterService converterService;
    private final RestTemplate restTemplate;

    public ConverterController(ConverterService converterService, RestTemplate restTemplate) {
        this.converterService = converterService;
        this.restTemplate = restTemplate;
    }


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
        return restTemplate.getForObject(LOCAL_HOST + 8081 + "/aggregate/get-aggregate-value" , BigDecimal.class);
    }

}
