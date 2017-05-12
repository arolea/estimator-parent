package com.learning.estimator.statisticsgenerator.config;

import com.learning.estimator.persistence.config.statistics.StatisticsPersistenceConfig;
import com.learning.estimator.persistencesd.config.JpaPersistenceConfig;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.config.TaskExecutorFactoryBean;

import javax.jms.Queue;

/**
 * Configuration for statistics computation node
 *
 * @author rolea
 */
@Configuration
@EnableJms
@EnableBatchProcessing
@Import(value = {StatisticsPersistenceConfig.class, JpaPersistenceConfig.class})
public class StatisticsGeneratorConfig {

    @Bean
    @Profile(value = {"dev", "prod"})
    public Queue projectQueue() {
        return new ActiveMQQueue("queue.statistics");
    }

    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository(ResourcelessTransactionManager transactionManager) throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(transactionManager);
        mapJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        return mapJobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(executorBuilder().getObject());
        return simpleJobLauncher;
    }

    @Bean
    public TaskExecutorFactoryBean executorBuilder() {
        TaskExecutorFactoryBean executorBuilder = new TaskExecutorFactoryBean();

        executorBuilder.setKeepAliveSeconds(120);
        executorBuilder.setPoolSize("100");
        executorBuilder.setQueueCapacity(100);
        executorBuilder.setRejectedExecutionHandler((task, executor) -> System.out.println("Task " + task + " rejected."));

        return executorBuilder;
    }

}
