package com.learning.estimator.datapublisher.config;

import com.learning.estimator.datapublisher.service.IDataPublisher;
import com.learning.estimator.datapublisher.service.impl.DataPublisher;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Queue;

/**
 * JMS published configuration
 *
 * @author rolea
 */
@Configuration
@ComponentScan(basePackageClasses = DataPublisher.class)
@EnableJms
public class DataPublisherConfig {

    @Autowired(required = false)
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Bean
    @Profile(value = {"dev", "prod"})
    public IDataPublisher getDevProdDataPublisher() {
        return new DataPublisher(jmsMessagingTemplate, projectQueue(), userQueue(), groupQueue(), taskQueue());
    }

    @Bean
    @Profile(value = {"test"})
    public IDataPublisher getTestDataPublisher() {
        return new IDataPublisher() {
            @Override
            public void publishUser(User user) {
            }

            @Override
            public void publishTask(Task task) {
            }

            @Override
            public void publishProject(Project project) {
            }

            @Override
            public void publishGroup(UserGroup group) {
            }
        };
    }

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
    @Qualifier("queue.tasks")
    @Profile(value = {"dev", "prod"})
    public Queue taskQueue() {
        return new ActiveMQQueue("queue.tasks");
    }
}
