package com.learning.estimator.datasplitter.service.impl;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.datasplitter.service.IDataSplitter;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.model.statistics.*;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Queue;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Data splitter implementation
 *
 * @author rolea
 */
@Component
@Profile(value = {"dev", "prod"})
public class DataSplitter implements IDataSplitter {

    private static final ILogger LOG = LogManager.getLogger(DataSplitter.class);
    @Autowired
    private StatisticsFacade statisticsFacade;
    @Autowired
    @Qualifier("queue.projects")
    private Queue projectQueue;
    @Autowired
    @Qualifier("queue.users")
    private Queue userQueue;
    @Autowired
    @Qualifier("queue.groups")
    private Queue groupQueue;
    @Autowired
    @Qualifier("queue.tasks")
    private Queue taskQueue;
    private ConcurrentMap<Long, ProjectDimension> projects;
    private ConcurrentMap<Long, UserDimension> users;
    private ConcurrentMap<Long, GroupDimension> groups;
    private volatile TimeDimension date;

    @PostConstruct
    public void init() {
        LOG.info("Starting data splitter...");
        LOG.info("Loading project dimension...");
        projects = statisticsFacade.findAllProjects().stream().collect(Collectors.toConcurrentMap(ProjectDimension::getProjectId, Function.identity()));
        LOG.info("Loading user dimension...");
        users = statisticsFacade.findAllUsers().stream().collect(Collectors.toConcurrentMap(UserDimension::getUserId, Function.identity()));
        LOG.info("Loading group dimension...");
        groups = statisticsFacade.findAllGroups().stream().collect(Collectors.toConcurrentMap(GroupDimension::getGroupId, Function.identity()));
        LOG.info("Getting current date");
        this.date = statisticsFacade.findTime(LocalDate.now());
        if (date == null) {
            LOG.info("Current date does not exist. Attempting to create current date.");
            try {
                this.date = statisticsFacade.saveTime(new TimeDimension(LocalDate.now()));
                LOG.info("Current date created.");
            } catch (Exception e) {
                //another node created the date entity for the current day'
                LOG.info("Current date created by another node. Updating local state.");
                this.date = statisticsFacade.findTime(LocalDate.now());
            }
        }
        LOG.info("Data splitter started...");
    }

    @Override
    @JmsListener(destination = "queue.projects")
    public void processProject(Project project) {
        LOG.info("Processing project...");
        ProjectDimension dimension = statisticsFacade.saveProject(new ProjectDimension(project.getProjectId(), project.getProjectName()));
        LOG.info("Updating local cache...");
        projects.putIfAbsent(dimension.getProjectId(), dimension);
    }

    @Override
    @JmsListener(destination = "queue.users")
    public void processUser(User user) {
        LOG.info("Processing user...");
        UserDimension dimension = statisticsFacade.saveUser(new UserDimension(user.getUserId(), user.getUsername()));
        LOG.info("Updating local cache...");
        users.putIfAbsent(dimension.getUserId(), dimension);
    }

    @Override
    @JmsListener(destination = "queue.groups")
    public void processGroup(UserGroup group) {
        LOG.info("Processing group...");
        GroupDimension dimension = statisticsFacade.saveGroup(new GroupDimension(group.getUserGroupId(), group.getUserGroupName()));
        LOG.info("Updating local cache...");
        groups.put(dimension.getGroupId(), dimension);
    }

    @Override
    @JmsListener(destination = "queue.tasks")
    public void processTask(Task task) {
        LOG.info("Processing task...");
        statisticsFacade.saveEstimateAccuracy(new EstimateAccuracyFact(Double.parseDouble(task.getEstimateAccuracy()),
                getUserDimension(task.getUser().getUserId()),
                getProjectDimension(task.getProject().getProjectId()),
                getGroupDimension(task.getProject().getUserGroup().getUserGroupId()),
                getCurrentTime()));
        statisticsFacade.saveLoggedTime(new LoggedTimeFact(task.getActualTimeSpent(),
                getUserDimension(task.getUser().getUserId()),
                getProjectDimension(task.getProject().getProjectId()),
                getGroupDimension(task.getProject().getUserGroup().getUserGroupId()),
                getCurrentTime()));
        statisticsFacade.saveVelocityPoint(new VelocityPointFact(task.getPoints(),
                getUserDimension(task.getUser().getUserId()),
                getProjectDimension(task.getProject().getProjectId()),
                getGroupDimension(task.getProject().getUserGroup().getUserGroupId()),
                getCurrentTime()));
    }

    private TimeDimension getCurrentTime() {
        if (LocalDate.now().equals(date.getDate()))
            return date;
        else {
            LOG.info("Current date expired. Attempting to update date");
            try {
                this.date = statisticsFacade.saveTime(new TimeDimension(LocalDate.now()));
                LOG.info("Date updated.");
            } catch (Exception e) {
                //another node created the date entity for the current day'
                LOG.info("Date updated by another node. Updating local state.");
                this.date = statisticsFacade.findTime(LocalDate.now());
            }
        }
        return date;
    }

    private UserDimension getUserDimension(Long userId) {
        UserDimension dimension = users.get(userId);
        if (dimension != null)
            return dimension;
        LOG.info("The user dimension with id " + userId + " was not found. Attempting remote fetch.");
        dimension = statisticsFacade.findUser(userId);
        if (dimension != null) {
            LOG.info("The user dimension with id " + userId + " was found remotely. Updating local cache...");
            users.put(userId, dimension);
            return dimension;
        }
        // this is an error and not a concurrency issue, since creating a task implies the user is already persisted.
        LOG.info("The user dimension with id " + userId + " was not found remotely. Unable to collect data for fact table.");
        throw new RuntimeException("The user dimension with id " + userId + " does not exist");
    }

    private ProjectDimension getProjectDimension(Long projectId) {
        ProjectDimension dimension = projects.get(projectId);
        if (dimension != null)
            return dimension;
        LOG.info("The project dimension with id " + projectId + " was not found. Attempting remote fetch.");
        dimension = statisticsFacade.findProject(projectId);
        if (dimension != null) {
            LOG.info("The project dimension with id " + projectId + " was found remotely. Updating local cache...");
            projects.put(projectId, dimension);
            return dimension;
        }
        // this is an error and not a concurrency issue, since creating a task implies the project is already persisted.
        LOG.info("The project dimension with id " + projectId + " was not found remotely. Unable to collect data for fact table.");
        throw new RuntimeException("The project dimension with id " + projectId + " does not exist");
    }

    private GroupDimension getGroupDimension(Long groupId) {
        GroupDimension dimension = groups.get(groupId);
        if (dimension != null)
            return dimension;
        LOG.info("The group dimension with id " + groupId + " was not found. Attempting remote fetch.");
        dimension = statisticsFacade.findGroup(groupId);
        if (dimension != null) {
            LOG.info("The group dimension with id " + groupId + " was found remotely. Updating local cache...");
            groups.put(groupId, dimension);
            return dimension;
        }
        // this is an error and not a concurrency issue, since creating a task implies the user group is already persisted.
        LOG.info("The group dimension with id " + groupId + " was not found remotely. Unable to collect data for fact table.");
        throw new RuntimeException("The group dimension with id " + groupId + " does not exist");
    }

}
