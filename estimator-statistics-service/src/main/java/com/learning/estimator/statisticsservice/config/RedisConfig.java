package com.learning.estimator.statisticsservice.config;

import com.learning.estimator.model.statistics.StatisticsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis configuration
 *
 * @author rolea
 */
@Configuration
@PropertySource(value = "classpath:redis-config.properties")
public class RedisConfig {

    @Value("${redis.hostname}")
    private String redisHostName;
    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHostName);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    @Bean
    @Primary
    public RedisTemplate<Integer, StatisticsResult> redisTemplate(RedisConnectionFactory redisCF) {
        RedisTemplate<Integer, StatisticsResult> redisTemplate = new RedisTemplate<Integer, StatisticsResult>();
        redisTemplate.setConnectionFactory(redisCF);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
