package com.learning.estimator.persistence.service.client;

import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.TaskStatus;

import java.util.List;

/**
 * Defines client side persistence operations for task entities
 *
 * @author rolea
 */
public interface ITaskServiceClientSide {

    Task saveTask(Task task);

    Task findTask(Long id);

    List<Task> findAllTasks();

    List<Task> findAllTasksByPredicate(Long userId, Long projectId, TaskStatus status);

    List<Task> findAllTasksByUser(Long userId);

    List<Task> findAllTasksByProject(Long projectId);

    List<Task> findAllTasksByStatus(TaskStatus status);

    List<Task> findTasks(int pageSize, int pageIndex);

    List<Task> findTasksByUser(Long userId, int pageIndex, int pageSize);

    List<Task> findTasksByProject(Long projectId, int pageIndex, int pageSize);

    List<Task> findTasksByStatus(TaskStatus status, int pageIndex, int pageSize);

    List<Task> findTasksByPredicate(Long userId, Long projectId, TaskStatus status, int pageIndex, int pageSize);

    void deleteTask(Long id);

    void deleteAllTasks();

    Long countTasks();

    Long countTasksByUser(Long userId);

    Long countTasksByProject(Long projectId);

    Long countTasksByStatus(TaskStatus status);

    Long countTasksByPredicate(Long userId, Long projectId, TaskStatus status);

}
