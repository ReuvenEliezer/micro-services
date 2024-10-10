package com.reuven.services;

import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SenderScheduler {

    private static final Logger logger = LogManager.getLogger(SenderScheduler.class);

    private static ScheduledExecutorService executorService;

    private final QueueService queueService;

    public SenderScheduler(QueueService queueService, @Value("${scheduler.sendEveryPeriodTimeInMillis}") Integer sendEveryPeriodTimeInMillis) {
        this.queueService = queueService;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::sendToAggregation, sendEveryPeriodTimeInMillis, sendEveryPeriodTimeInMillis, TimeUnit.MILLISECONDS);
    }


    private void sendToAggregation() {
        queueService.sendAll();
    }

    @PreDestroy
    private static void preDestroy() {
        if (executorService != null) {
            logger.info("preDestroy");
            executorService.shutdown();
            executorService = null;
        }
    }

}
