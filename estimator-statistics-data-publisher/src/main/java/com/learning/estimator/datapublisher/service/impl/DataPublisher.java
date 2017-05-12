package com.learning.estimator.datapublisher.service.impl;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.datapublisher.service.IDataPublisher;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Queue;

/**
 * Data published implementation
 *
 * @author rolea
 */
public class DataPublisher implements IDataPublisher {

    private static final ILogger LOG = LogManager.getLogger(DataPublisher.class);
    private JmsMessagingTemplate jmsMessagingTemplate;
    private Queue projectQueue;
    private Queue userQueue;
    private Queue groupQueue;
    private Queue taskQueue;

    public DataPublisher(JmsMessagingTemplate jmsMessagingTemplate, Queue projectQueue, Queue userQueue, Queue groupQueue, Queue taskQueue) {
        LOG.info("Starting data publisher service...");
        this.jmsMessagingTemplate = jmsMessagingTemplate;
        this.projectQueue = projectQueue;
        this.userQueue = userQueue;
        this.groupQueue = groupQueue;
        this.taskQueue = taskQueue;
        LOG.info("Data publisher service started");
    }

    @Override
    public void publishProject(Project project) {
        LOG.info("Publishing project " + project.getProjectName());
        this.send(projectQueue, project);
    }

    @Override
    public void publishUser(User user) {
        LOG.info("Publishing user " + user.getUsername());
        this.send(userQueue, user);
    }

    @Override
    public void publishGroup(UserGroup group) {
        LOG.info("Publishing group " + group.getUserGroupName());
        this.send(groupQueue, group);
    }

    @Override
    public void publishTask(Task task) {
        LOG.info("Publishing task");
        this.send(taskQueue, task);
    }

    public void send(Queue queue, Object payload) {
        try {
            this.jmsMessagingTemplate.convertAndSend(queue, payload);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

}
