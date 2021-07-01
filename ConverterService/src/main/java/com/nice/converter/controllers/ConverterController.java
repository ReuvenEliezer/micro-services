package com.nice.converter.controllers;

import com.nice.converter.services.ConverterService;
import com.nice.converter.utils.WsAddressConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(WsAddressConstants.convertLogicUrl)
public class ConverterController {

    @Autowired
    private ConverterService converterService;


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

}
