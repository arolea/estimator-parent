package com.learning.estimator.statisticsclient.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for client side statistics operations
 *
 * @author rolea
 */
@Configuration
@EnableDiscoveryClient
@PropertySource("classpath:statistics-api-config.properties")
@ComponentScan(basePackages = {"com.learning.estimator.statisticsclient.service"})
@RibbonClient(name = "statisticsservice")
public class StatisticsClientConfig {

    @Bean(name = "statisticstemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        return template;
    }

}
