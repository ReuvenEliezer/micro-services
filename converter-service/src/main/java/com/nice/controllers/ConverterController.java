package com.nice.controllers;

import com.nice.services.ConverterService;
import com.nice.utils.WsAddressConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RestController
@RequestMapping(WsAddressConstants.convertLogicUrl)
public class ConverterController {

    private static final Logger logger = LogManager.getLogger(ConverterController.class);
    private static final String LOCAL_HOST = "http://localhost:";

    private static final boolean IS_RUNNING_INSIDE_DOCKER = isRunningInsideDocker();

    private final int AGG_SERVER_PORT;

    private final ConverterService converterService;
    private final RestTemplate restTemplate;


    public ConverterController(ConverterService converterService, RestTemplate restTemplate,
                               @Value("${aggregation.server.port}") int aggServerPort,
                               @Value("${spring.boot.admin.client.url}") String adminUrl
    ) {
        logger.info("ConverterController: adminUrl: {}", adminUrl);
        this.converterService = converterService;
        this.restTemplate = restTemplate;
        this.AGG_SERVER_PORT = aggServerPort;
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
        logger.info("call-aggregate-service: getAggregateValue runningInsideDocker: {}", IS_RUNNING_INSIDE_DOCKER);
        String baseUrl = IS_RUNNING_INSIDE_DOCKER ? "http://aggregation-service:" : LOCAL_HOST;
        return restTemplate.getForObject(baseUrl + AGG_SERVER_PORT + "/aggregate/get-aggregate-value", BigDecimal.class);
    }

    static Boolean isRunningInsideDocker() {
        try (Stream<String> stream = Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }

}
