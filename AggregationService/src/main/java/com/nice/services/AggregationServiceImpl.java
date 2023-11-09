package com.nice.services;

import com.nice.entities.WriterTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Writer;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AggregationServiceImpl implements AggregationService {

    private static final Logger logger = LogManager.getLogger(AggregationServiceImpl.class);
    private final AtomicReference<BigDecimal> valueHolder = new AtomicReference<>();

    @Value("${writerClassName}")
    private String writerClassName;

    @Override
    public void aggregate(BigDecimal bigDecimal) {
        if (valueHolder.get() == null) {
            valueHolder.set(bigDecimal);
            logger.info("init value to {}", bigDecimal);
        } else {
            valueHolder.getAndAccumulate(bigDecimal, BigDecimal::add);
        }
        BigDecimal valueToPrint = valueHolder.get();
        Class<? extends Writer> writer = WriterTypeEnum.getWriter(writerClassName);
        if (writer == null) {
            throw new UnsupportedOperationException(writerClassName + " not supporter");
        }
        try (Writer writer1 = writer.newInstance()) {
            writer1.write(valueToPrint.intValue());
        } catch (Exception e) {
            throw new RuntimeException(String.format("failed to write value:%s to %s, Exception=%s", valueToPrint, writerClassName, e));
        }
    }

    @Override
    public BigDecimal getAggregateValue() {
        logger.info("getAggregateValue");
        return valueHolder.get();
    }

}
