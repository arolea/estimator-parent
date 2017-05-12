package com.learning.estimator.datasplitter.config;

import com.learning.estimator.persistence.config.statistics.StatisticsPersistenceConfig;
import com.learning.estimator.persistencesd.config.JpaPersistenceConfig;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;

/**
 * Configuration for data splitter
 *
 * @author rolea
 */
@Configuration
@EnableJms
@Import(value = {StatisticsPersistenceConfig.class, JpaPersistenceConfig.class})
public class DataSplitterConfig {

    @Bean
    @Qualifier("queue.projects")
    @Profile(value = {"dev", "prod"})
    public Queue projectQueue() {
        return new ActiveMQQueue("queue.projects");
    }

    @Bean
    @Qualifier("queue.users")
    @Profile(value = {"dev", "prod"})
    public Queue userQueue() {
        return new ActiveMQQueue("queue.users");
    }

    @Bean
    @Qualifier("queue.groups")
    @Profile(value = {"dev", "prod"})
    public Queue groupQueue() {
        return new ActiveMQQueue("queue.groups");
    }

    @Bean
    @Qualifier("queue.test")
    public Queue testQueue() {
        return new ActiveMQQueue("queue.test");
    }

    @Bean
    @Qualifier("queue.tasks")
    @Profile(value = {"dev", "prod"})
    public Queue taskQueue() {
        return new ActiveMQQueue("queue.tasks");
    }

}
