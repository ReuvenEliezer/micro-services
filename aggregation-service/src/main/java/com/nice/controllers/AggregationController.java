package com.nice.controllers;

import com.nice.services.AggregationService;
import com.nice.utils.WsAddressConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(WsAddressConstants.aggregateLogicUrl)
public class AggregationController {

    private static final Logger logger = LogManager.getLogger(AggregationController.class);

    private final AggregationService aggregationService;

    public AggregationController(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping(value = "{value:.+}")
    public void aggregate(@PathVariable BigDecimal value) {
        aggregationService.aggregate(value);
    }

    @GetMapping(value = "get-aggregate-value")
    public BigDecimal getAggregateValue() {
        logger.info("getAggregateValue");
        return aggregationService.getAggregateValue();
    }

}
