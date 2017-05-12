package com.learning.estimator.persistenceclient.service.impl;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.common.session.IEstimatorSession;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.TaskStatus;
import com.learning.estimator.persistence.operations.TaskOperations;
import com.learning.estimator.persistence.service.client.ITaskServiceClientSide;
import com.learning.estimator.persistenceclient.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Consumes persistence operations for tasks
 *
 * @author rolea
 */
@Component
public class TaskService implements ITaskServiceClientSide {

    private static final ILogger LOG = LogManager.getLogger(TaskService.class);
    @javax.annotation.Resource(name = "persistencetemplate")
    protected RestTemplate restTemplate;
    @Autowired
    private IEstimatorSession session;

    @Override
    @WithPersistenceTryCatch
    public Task saveTask(Task project) {
        ResponseEntity<Task> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.SAVE_TASK.getId()), HttpMethod.POST, new HttpEntity<Task>(project, Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode()))
            LOG.debug("Create or update request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public Task findTask(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("taskId", id);
        ResponseEntity<Task> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.FIND_TASK.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task.class, id);
        if (!HttpStatus.OK.equals(response.getStatusCode()))
            LOG.debug("Read by id request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasks() {
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.FIND_ALL_TASKS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByPredicate(Long userId, Long projectId, TaskStatus status) {
        Map<String, Object> params = new HashMap<>(4, 1);
        params.put("userId", userId);
        params.put("projectId", projectId);
        params.put("status", status);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.FIND_ALL_BY_CRITERIA.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByUser(Long userId) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", userId);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.FIND_ALL_BY_USER.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByProject(Long projectId) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("projectId", projectId);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.FIND_ALL_BY_PROJECT.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findAllTasksByStatus(TaskStatus status) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("status", status);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.FIND_ALL_BY_STATUS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasks(int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(4, 1);
        params.put("userId", (Long) session.getDataFromSession("user_id"));
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.PAGINATE_TASKS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByUser(Long userId, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(4, 1);
        params.put("userId", userId);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.PAGINATE_BY_USER.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByProject(Long projectId, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(4, 1);
        params.put("projectId", projectId);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.PAGINATE_BY_PROJECT.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByStatus(TaskStatus status, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(4, 1);
        params.put("status", status);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.PAGINATE_BY_STATUS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Task> findTasksByPredicate(Long userId, Long projectId, TaskStatus status, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(6, 1);
        params.put("userId", userId);
        params.put("projectId", projectId);
        params.put("status", status);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        ResponseEntity<Task[]> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.PAGINATE_BY_CRITERIA.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Task[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteTask(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("taskId", id);
        ResponseEntity<Void> response = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.DELETE_TASK.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class, params);
        if (!HttpStatus.NO_CONTENT.equals(response.getStatusCode()))
            LOG.debug("Delete request did not succeed. Status code " + response.getStatusCode());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllTasks() {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", (Long) session.getDataFromSession("user_id"));
        restTemplate.exchange((String) session.getDataFromSession(TaskOperations.DELETE_ALL_TASKS.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class, params);
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasks() {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", (Long) session.getDataFromSession("user_id"));
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.COUNT_TASKS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class, params);
        return orderCounter.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByUser(Long userId) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", userId);
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.COUNT_TASKS_BY_USER.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class, params);
        return orderCounter.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByProject(Long projectId) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("projectId", projectId);
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.COUNT_TASKS_BY_PROJECT.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class, params);
        return orderCounter.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByStatus(TaskStatus status) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("status", status);
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.COUNT_TASKS_BY_STATUS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class, params);
        return orderCounter.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTasksByPredicate(Long userId, Long projectId, TaskStatus status) {
        Map<String, Object> params = new HashMap<>(4, 1);
        params.put("userId", userId);
        params.put("projectId", projectId);
        params.put("status", status);
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(TaskOperations.COUNT_TASKS_BY_CRITERIA.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class, params);
        return orderCounter.getBody();
    }

}
