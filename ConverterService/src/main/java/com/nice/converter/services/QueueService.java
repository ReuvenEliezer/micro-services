package com.nice.converter.services;

import java.math.BigDecimal;

public interface QueueService {
    void sendAll();

    void put(BigDecimal bigDecimal);
}
