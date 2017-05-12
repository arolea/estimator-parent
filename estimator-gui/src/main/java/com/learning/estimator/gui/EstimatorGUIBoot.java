package com.learning.estimator.gui;

import com.learning.estimator.persistence.config.client.CorePersistenceConfigClientSide;
import com.learning.estimator.persistenceclient.config.RestClientPersistenceConfig;
import com.learning.estimator.statisticsclient.config.StatisticsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Boot for GUI
 *
 * @author rolea
 */
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
@Import(value = {CorePersistenceConfigClientSide.class, RestClientPersistenceConfig.class, StatisticsClientConfig.class})
public class EstimatorGUIBoot {

    public static void main(String[] args) {
        SpringApplication.run(EstimatorGUIBoot.class, args);
    }

}
