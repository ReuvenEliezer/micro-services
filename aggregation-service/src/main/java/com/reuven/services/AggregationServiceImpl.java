package com.reuven.services;

import com.reuven.entities.WriterTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, BigDecimal> redisTemplate;
    private static final String REDIS_KEY = "aggregate-value";

    public AggregationServiceImpl(@Value("${writer-class-name}") String writerClassName,
                                  RedisTemplate<String, BigDecimal> redisTemplate) {
        this.writerClassName = writerClassName;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void aggregate(BigDecimal bigDecimal) {
        if (valueHolder.get() == null) {
            valueHolder.set(bigDecimal);
            logger.info("init value to {}", bigDecimal);
        } else {
            valueHolder.getAndAccumulate(bigDecimal, BigDecimal::add);
        }

        BigDecimal aggValue = redisTemplate.opsForValue().get(REDIS_KEY);
        redisTemplate.opsForValue().set(REDIS_KEY,
                Optional.ofNullable(aggValue)
                        .map(aggV -> aggV.add(bigDecimal))
                        .orElse(bigDecimal)
        );
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
        BigDecimal bigDecimal = valueHolder.get() != null ? valueHolder.get() : BigDecimal.ZERO;
        logger.info("getAggregateValue() local value: '{}'", bigDecimal);
        BigDecimal bigDecimal1 = redisTemplate.opsForValue().get(REDIS_KEY);
        logger.info("getAggregateValue from redis: '{}'", bigDecimal1);
        return bigDecimal1 != null ? bigDecimal1 : BigDecimal.ZERO;
    }

}
