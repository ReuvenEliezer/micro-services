package com.nice.controllers;

import com.nice.services.AggregationService;
import com.nice.utils.WsAddressConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(WsAddressConstants.aggregateLogicUrl)
public class AggregationController {

    @Autowired
    private AggregationService aggregationService;

    @GetMapping(value = "{value:.+}")
    public void aggregate(@PathVariable BigDecimal value) {
        aggregationService.aggregate(value);
    }

}
