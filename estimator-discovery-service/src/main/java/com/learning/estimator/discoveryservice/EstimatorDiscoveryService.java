package com.learning.estimator.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka server for discoverability
 * See application.yml for configuration
 * See http://localhost:8761 for eureka dashboard
 *
 * @author rolea
 */
@SpringBootApplication
@EnableEurekaServer
public class EstimatorDiscoveryService {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EstimatorDiscoveryService.class, args);
    }

}

