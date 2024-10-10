package com.reuven.services;

import java.math.BigDecimal;

public interface AggregationService {
    void aggregate(BigDecimal bigDecimal);
    BigDecimal getAggregateValue();

}
