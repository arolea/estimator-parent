package com.learning.estimator.persistence.config.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Core persistence configuration for server side implementation ( the facade that is exposed to service implementors )
 *
 * @author rolea
 */
@Configuration
@ComponentScan(basePackages = {"com.learning.estimator.persistence.facade.server"})
public class CorePersistenceConfigServerSide {

}


