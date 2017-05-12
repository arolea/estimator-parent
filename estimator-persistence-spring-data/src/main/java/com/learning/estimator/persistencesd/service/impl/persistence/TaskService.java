package com.learning.estimator.persistencesd.service.impl.persistence;

import com.learning.estimator.common.exceptions.persistence.EntityNotFoundException;
import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.TaskStatus;
import com.learning.estimator.persistence.service.server.ITaskServiceServerSide;
import com.learning.estimator.persistencesd.repositories.persistence.ITaskRepository;
import com.learning.estimator.persistencesd.utils.TaskPredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation for task persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class TaskService implements ITaskServiceServerSide {

    @Autowired
    private ITaskRepository repository;

    @Override
    @WithPersistenceTryCatch
    public Task saveTask(Task task) {
        return repository.save(task);
    }

    @Override
    @WithPersistenceTryCatch
    public Task findTask(Long id) {
        Task task = repository.findTaskByTaskId(id);
        if (task == null)
            throw new EntityNotFoundException("The task with id " + id + " was not found");
        return task;
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasks() {
        return repository.findAllTasks();
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByPredicate(Long userId, Long projectId, TaskStatus status) {
        return repository.findAll(TaskPredicateBuilder.build(userId, projectId, status));
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByUser(Long userId) {
        return repository.findAllByUser(userId);
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByProject(Long projectId) {
        return repository.findAllByProject(projectId);
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByStatus(TaskStatus status) {
        return repository.findAllByTaskStatus(status);
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasks(int pageIndex, int pageSize) {
        return repository.findTasks(new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "user.username", "project.projectName", "taskStatus", "taskName")).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByUser(Long userId, int pageIndex, int pageSize) {
        return repository.findTasksByUser(new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "user.username", "project.projectName", "taskStatus", "taskName"), userId).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByProject(Long projectId, int pageIndex, int pageSize) {
        return repository.findTasksByProject(new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "user.username", "project.projectName", "taskStatus", "taskName"), projectId).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByStatus(TaskStatus status, int pageIndex, int pageSize) {
        return repository.findTasksByStatus(new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "user.username", "project.projectName", "taskStatus", "taskName"), status).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteTask(Long id) {
        repository.delete(id);
    }

    @WithPersistenceTryCatch
    public void deleteAllTasksForUser(Long userId) {
        repository.deleteAllByUserUserId(userId);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllTasks() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllTasksByUserId(Long userId) {
        repository.deleteAllByUserUserId(userId);
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasks() {
        return repository.count();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByUser(Long userId) {
        return repository.countByUserUserId(userId);
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByPredicate(Long userId, Long projectId, TaskStatus status, int pageIndex, int pageSize) {
        return repository.customFindByPredicate(TaskPredicateBuilder.build(userId, projectId, status), new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "user.username", "project.projectName", "taskStatus", "taskName")).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByProject(Long projectId) {
        return repository.countByProjectProjectId(projectId);
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByStatus(TaskStatus status) {
        return repository.countByTaskStatus(status);
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByPredicate(Long userId, Long projectId, TaskStatus status) {
        return repository.count(TaskPredicateBuilder.build(userId, projectId, status));
    }

}
