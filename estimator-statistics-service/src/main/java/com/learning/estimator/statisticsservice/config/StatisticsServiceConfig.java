package com.learning.estimator.statisticsservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.statisticsservice.service.IStatisticsService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;

/**
 * Configuration for statistics service
 *
 * @author rolea
 */
@Configuration
@EnableJms
@EnableDiscoveryClient
@EnableCircuitBreaker
public class StatisticsServiceConfig {

    @Bean
    @Profile(value = {"dev", "prod"})
    public Queue projectQueue() {
        return new ActiveMQQueue("queue.statistics");
    }

    @Bean
    @Profile(value = "test")
    public IStatisticsService getTestStatisticsService() {
        return new IStatisticsService() {
            @Override
            public StatisticsResult requestStatisticResult(Integer id) {
                System.out.println("Requesting result");
                return null;
            }

            @Override
            public Integer requestStatisticComputation(StatisticsQuery query) {
                System.out.println(query);
                return 1;
            }
        };
    }

    @Bean
    public ObjectMapper getJacksonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

}
