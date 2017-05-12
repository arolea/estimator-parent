package com.learning.estimator.persistence.facade.client;

import com.learning.estimator.model.entities.*;
import com.learning.estimator.persistence.service.client.IProjectServiceClientSide;
import com.learning.estimator.persistence.service.client.ITaskServiceClientSide;
import com.learning.estimator.persistence.service.client.IUserGroupServiceClientSide;
import com.learning.estimator.persistence.service.client.IUserServiceClientSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Facade for client side persistence operations
 *
 * @author rolea
 */
@Component
public class PersistenceFacadeClientSide implements IUserGroupServiceClientSide, IUserServiceClientSide, IProjectServiceClientSide, ITaskServiceClientSide {

    @Autowired
    private IUserGroupServiceClientSide userGroupService;
    @Autowired
    private IUserServiceClientSide userService;
    @Autowired
    private IProjectServiceClientSide projectService;
    @Autowired
    private ITaskServiceClientSide taskService;

    /*
     * Task level persistence operations
     */
    @Override
    public Task saveTask(Task task) {
        return taskService.saveTask(task);
    }

    @Override
    public Task findTask(Long id) {
        return taskService.findTask(id);
    }

    @Override
    public List<Task> findAllTasks() {
        return taskService.findAllTasks();
    }

    @Override
    public List<Task> findAllTasksByPredicate(Long userId, Long projectId, TaskStatus status) {
        return taskService.findAllTasksByPredicate(userId, projectId, status);
    }

    @Override
    public List<Task> findAllTasksByUser(Long userId) {
        return taskService.findAllTasksByUser(userId);
    }

    @Override
    public List<Task> findAllTasksByProject(Long projectId) {
        return taskService.findAllTasksByProject(projectId);
    }

    @Override
    public List<Task> findAllTasksByStatus(TaskStatus status) {
        return taskService.findAllTasksByStatus(status);
    }

    @Override
    public List<Task> findTasks(int pageIndex, int pageSize) {
        return taskService.findTasks(pageIndex, pageSize);
    }

    @Override
    public List<Task> findTasksByUser(Long userId, int pageIndex, int pageSize) {
        return taskService.findTasksByUser(userId, pageIndex, pageSize);
    }

    @Override
    public List<Task> findTasksByProject(Long projectId, int pageIndex, int pageSize) {
        return taskService.findTasksByProject(projectId, pageIndex, pageSize);
    }

    @Override
    public List<Task> findTasksByStatus(TaskStatus status, int pageIndex, int pageSize) {
        return taskService.findTasksByStatus(status, pageIndex, pageSize);
    }

    @Override
    public List<Task> findTasksByPredicate(Long userId, Long projectId, TaskStatus status, int pageIndex, int pageSize) {
        return taskService.findTasksByPredicate(userId, projectId, status, pageIndex, pageSize);
    }

    @Override
    public void deleteTask(Long id) {
        taskService.deleteTask(id);
    }

    @Override
    public void deleteAllTasks() {
        taskService.deleteAllTasks();
    }

    @Override
    public Long countTasks() {
        return taskService.countTasks();
    }

    @Override
    public Long countTasksByUser(Long userId) {
        return taskService.countTasksByUser(userId);
    }

    @Override
    public Long countTasksByProject(Long projectId) {
        return taskService.countTasksByProject(projectId);
    }

    @Override
    public Long countTasksByStatus(TaskStatus status) {
        return taskService.countTasksByStatus(status);
    }

    @Override
    public Long countTasksByPredicate(Long userId, Long projectId, TaskStatus status) {
        return taskService.countTasksByPredicate(userId, projectId, status);
    }

    /*
     * Project level persistence operations
     */
    @Override
    public Project saveProject(Project project) {
        return projectService.saveProject(project);
    }

    @Override
    public Project findProject(Long id) {
        return projectService.findProject(id);
    }

    @Override
    public Project findProjectByName(String name) {
        return projectService.findProjectByName(name);
    }

    @Override
    public List<Project> findAllProjects() {
        return projectService.findAllProjects();
    }

    @Override
    public List<Project> findProjects(int pageIndex, int pageSize) {
        return projectService.findProjects(pageIndex, pageSize);
    }

    @Override
    public void deleteProject(Long id) {
        projectService.deleteProject(id);
    }

    @Override
    public void deleteAllProjects() {
        projectService.deleteAllProjects();
    }

    @Override
    public Long countProjects() {
        return projectService.countProjects();
    }

    /*
     * User level persistence operations
     */
    @Override
    public User saveUser(User user) {
        return userService.saveUser(user);
    }

    @Override
    public User findUser(Long id) {
        return userService.findUser(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @Override
    public List<User> findUsers(int pageIndex, int pageSize) {
        return userService.findUsers(pageIndex, pageSize);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Override
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @Override
    public Long countUsers() {
        return userService.countUsers();
    }

    /*
     * User group level persistence operations
     */
    @Override
    public UserGroup saveUserGroup(UserGroup group) {
        return userGroupService.saveUserGroup(group);
    }

    @Override
    public UserGroup findUserGroup(Long id) {
        return userGroupService.findUserGroup(id);
    }

    @Override
    public UserGroup findUserGroupByName(String name) {
        return userGroupService.findUserGroupByName(name);
    }

    @Override
    public List<UserGroup> findAllUserGroups() {
        return userGroupService.findAllUserGroups();
    }

    @Override
    public List<UserGroup> findUserGroups(int pageIndex, int pageSize) {
        return userGroupService.findUserGroups(pageIndex, pageSize);
    }

    @Override
    public void deleteUserGroup(Long id) {
        userGroupService.deleteUserGroup(id);
    }

    @Override
    public void deleteAllUserGroups() {
        userGroupService.deleteAllUserGroups();
    }

    @Override
    public Long countUserGroups() {
        return userGroupService.countUserGroups();
    }
}
