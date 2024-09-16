package com.nice.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.Delay;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;

@Configuration
public class RedisConfig {

//    @Bean
//    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<K, V> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        return template;
//    }

    @Bean
    public RedisTemplate<String, BigDecimal> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, BigDecimal> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(redisConnectionFactory).build();
//    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Value("${spring.data.redis.host}") String redisHost,
                                                         @Value("${spring.data.redis.port}") int redisPort) {
        RedisStaticMasterReplicaConfiguration redisConfiguration = new RedisStaticMasterReplicaConfiguration(redisHost, redisPort);
//        RedisStaticMasterReplicaConfiguration redisConfiguration = new RedisStaticMasterReplicaConfiguration(internalWriterHost, internalWriterPort);
//        redisConfiguration.addNode(internalReaderHost, internalReaderPort);
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMinIdle(1);
        poolConfig.setMaxTotal(5);
        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .readFrom(ReadFrom.REPLICA_PREFERRED)
//                .commandTimeout(Duration.ofMillis(REDIS_TIMEOUT_IN_MILLIS))
                .clientOptions(ClientOptions.builder()
                        .autoReconnect(true)
                        .build())
                .clientResources(DefaultClientResources.builder()
                        .reconnectDelay(Delay.exponential())
                        .build())
                .build();
//        return new LettuceConnectionFactory("localhost", 6379);
        return new LettuceConnectionFactory(redisConfiguration, clientConfiguration);
    }
}
