package com.nice.converter.services;

import java.math.BigDecimal;

public interface ConverterService {
    BigDecimal convertStr(String str);

    BigDecimal convertHex(String hex);

    BigDecimal convertFraction(String fraction);

}
