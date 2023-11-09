package com.nice.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SenderScheduler {

    private static final Logger logger = LogManager.getLogger(SenderScheduler.class);

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Value("${scheduler.sendEveryPeriodTimeInMillis}")
    private Integer sendEveryPeriodTimeInMillis;

    @Autowired
    private QueueService queueService;


    @PostConstruct
    private void init() {
        executorService.scheduleWithFixedDelay(this::sendToAggregation, sendEveryPeriodTimeInMillis, sendEveryPeriodTimeInMillis, TimeUnit.MILLISECONDS);
        logger.info("init");
    }

    private void sendToAggregation() {
        queueService.sendAll();
    }

    @PreDestroy
    private void preDestroy() {
        if (executorService != null) {
            logger.info("preDestroy");
            executorService.shutdown();
        }
        executorService = null;
    }

}
