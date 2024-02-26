package com.nice.services;

import com.nice.entities.WriterTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Writer;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AggregationServiceImpl implements AggregationService {

    private static final Logger logger = LogManager.getLogger(AggregationServiceImpl.class);
    private static final AtomicReference<BigDecimal> valueHolder = new AtomicReference<>();
    private final String writerClassName;

    public AggregationServiceImpl(@Value("${writer-class-name}") String writerClassName) {
        this.writerClassName = writerClassName;
    }

    @Override
    public void aggregate(BigDecimal bigDecimal) {
        if (valueHolder.get() == null) {
            valueHolder.set(bigDecimal);
            logger.info("init value to {}", bigDecimal);
        } else {
            valueHolder.getAndAccumulate(bigDecimal, BigDecimal::add);
        }
        BigDecimal valueToPrint = valueHolder.get();
        Class<? extends Writer> writer = Optional.ofNullable(WriterTypeEnum.getWriter(writerClassName))
                .orElseThrow(() -> new UnsupportedOperationException(writerClassName + " not supported"));
        try (Writer writer1 = writer.getDeclaredConstructor().newInstance()) {
            writer1.write(valueToPrint.intValue());
        } catch (Exception e) {
            throw new RuntimeException(String.format("failed to write value:%s to %s, Exception=%s", valueToPrint, writerClassName, e));
        }
    }

    @Override
    public BigDecimal getAggregateValue() {
        logger.info("getAggregateValue");
        return valueHolder.get() != null ? valueHolder.get() : BigDecimal.ZERO;
    }

}
