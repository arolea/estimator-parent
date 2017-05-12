package com.learning.estimator.persistenceclient.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for client side persistence operations
 *
 * @author rolea
 */
@Configuration
@EnableDiscoveryClient
@PropertySource("classpath:api-config.properties")
@ComponentScan(basePackages = {"com.learning.estimator.persistenceclient.service"})
@RibbonClient(name = "persistenceservice")
public class RestClientPersistenceConfig {

    @Bean(name = "persistencetemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        return template;
    }

}
