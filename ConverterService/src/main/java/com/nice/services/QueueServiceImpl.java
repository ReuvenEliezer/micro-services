package com.nice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class QueueServiceImpl implements QueueService {

    private final static Logger logger = LogManager.getLogger(QueueServiceImpl.class);

    private BlockingDeque<BigDecimal> blockingDeque = new LinkedBlockingDeque();
    private static final String localhost = "http://localhost:";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${aggregation.server.port}")
    private Integer aggServerPort;

    @Value("${aggregation.url}")
    private String aggUrl;


    @Override
    public void sendAll() {
        logger.info("sendAll queue values to aggregation service");
        while (!blockingDeque.isEmpty()) {
            BigDecimal value = blockingDeque.poll();
            try {
                restTemplate.getForObject(localhost + aggServerPort + aggUrl + value, Void.class);
            } catch (Exception e) {
                logger.error("failed to sent value '{}' to aggregation server", value, e);
            }
        }
    }

    @Override
    public void put(BigDecimal bigDecimal) {
        try {
            blockingDeque.put(bigDecimal);
        } catch (InterruptedException e) {
            throw new RuntimeException(String.format("failed to insert value %s %s", bigDecimal, e));
        }
    }
}
