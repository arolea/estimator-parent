package com.learning.estimator.persistence.config.client;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Core persistence configuration for client side implementation ( the facade that is exposed to clients )
 *
 * @author rolea
 */
@Configuration
@ComponentScan(basePackages = {"com.learning.estimator.persistence.facade.client"})
public class CorePersistenceConfigClientSide {

}
