package com.nice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConverterServiceImpl implements ConverterService {

    private static final Logger logger = LogManager.getLogger(ConverterServiceImpl.class);

    private final QueueService queueService;

    public ConverterServiceImpl(QueueService queueService) {
        this.queueService = queueService;
    }

    @Override
    public BigDecimal convertStr(String str) {
        validateStr(str);
        int result = 0;
        for (int value : str.toCharArray()) {
            result += value;
        }
        BigDecimal bigDecimal = new BigDecimal(result);
        logger.info("put value {} to queue", bigDecimal);
        queueService.put(bigDecimal);
        return bigDecimal;
    }

    @Override
    public BigDecimal convertHex(String hex) {
        validateHex(hex);
        long value = Long.parseLong(hex, 16);
        BigDecimal bigDecimal = new BigDecimal(value);
        queueService.put(bigDecimal);
        return bigDecimal;
    }

    @Override
    public BigDecimal convertFraction(String fraction) {
        validateFraction(fraction);
        String[] split = fraction.split("/");
        BigDecimal value = BigDecimal.valueOf(Long.valueOf(split[0]).longValue()).divide(BigDecimal.valueOf(Long.valueOf(split[1]).longValue()));
        validatePositiveNumber(value);
        queueService.put(value);
        return value;
    }

    private void validatePositiveNumber(BigDecimal value) {
        if (value.signum() <= 0) {
            String message = String.format("input value '%s' is negative or zero, please insert positive number", value);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateHex(String hex) {
        Pattern p = Pattern.compile("[0-9a-fA-F]+");
        Matcher m = p.matcher(hex);
        if (!m.matches()) {
            String message = String.format("hex '%s' not valid", hex);
            logger.error(message);
            throw new IllegalArgumentException(message);

        }
    }

    private void validateStr(String str) {
        if (!str.chars().allMatch(Character::isLetter)) {
            String message = String.format("str '%s' contains a numeric value", str);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateFraction(String fraction) {
        if (!fraction.contains("/") || fraction.split("/").length != 2) {
            String message = String.format("fraction '%s' not valid. expected '/' between 2 numbers", fraction);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (Integer.valueOf(fraction.split("/")[1]).equals(0)){
            String message = "unable to divide by zero";
            logger.error(message);
            throw new ArithmeticException(message);
        }
    }
}
