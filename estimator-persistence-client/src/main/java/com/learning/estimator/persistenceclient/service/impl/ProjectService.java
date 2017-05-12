package com.learning.estimator.persistenceclient.service.impl;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.common.session.IEstimatorSession;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.persistence.operations.ProjectOperations;
import com.learning.estimator.persistence.service.client.IProjectServiceClientSide;
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
 * Consumes persistence operations for projects
 *
 * @author rolea
 */
@Component
public class ProjectService implements IProjectServiceClientSide {

    private static final ILogger LOG = LogManager.getLogger(ProjectService.class);
    @javax.annotation.Resource(name = "persistencetemplate")
    protected RestTemplate restTemplate;
    @Autowired
    private IEstimatorSession session;

    @Override
    @WithPersistenceTryCatch
    public Project saveProject(Project project) {
        ResponseEntity<Project> response = restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.SAVE_PROJECT.getId()), HttpMethod.POST, new HttpEntity<Project>(project, Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Project.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode()))
            LOG.debug("Create or update request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public Project findProject(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("projectId", id);
        ResponseEntity<Project> response = restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.FIND_PROJECT.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Project.class, params);
        if (!HttpStatus.OK.equals(response.getStatusCode()))
            LOG.debug("Read by id request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public Project findProjectByName(String name) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("name", name);
        ResponseEntity<Project> response = restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.FIND_PROJECT_BY_NAME.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Project.class, params);
        if (!HttpStatus.OK.equals(response.getStatusCode()))
            LOG.debug("Read by name request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public List<Project> findAllProjects() {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", (Long) session.getDataFromSession("user_id"));
        ResponseEntity<Project[]> response = restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.FIND_ALL_PROJECTS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Project[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<Project> findProjects(int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(3, 1);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        ResponseEntity<Project[]> response = restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.PAGINATE_PROJECTS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Project[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteProject(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("projectId", id);
        ResponseEntity<Void> response = restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.DELETE_PROJECT.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class, params);
        if (!HttpStatus.NO_CONTENT.equals(response.getStatusCode()))
            LOG.debug("Delete request did not succeed. Status code " + response.getStatusCode());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllProjects() {
        restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.DELETE_ALL_PROJECTS.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class);
    }

    @Override
    @WithPersistenceTryCatch
    public Long countProjects() {
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(ProjectOperations.COUNT_PROJECTS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class);
        return orderCounter.getBody();
    }

}
