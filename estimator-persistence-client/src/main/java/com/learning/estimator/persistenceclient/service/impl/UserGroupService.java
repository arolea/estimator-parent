package com.learning.estimator.persistenceclient.service.impl;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.common.session.IEstimatorSession;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistence.operations.GroupOperations;
import com.learning.estimator.persistence.service.client.IUserGroupServiceClientSide;
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
 * Consumes persistence operations for user groups
 *
 * @author rolea
 */
@Component
public class UserGroupService implements IUserGroupServiceClientSide {

    private static final ILogger LOG = LogManager.getLogger(UserGroupService.class);
    @javax.annotation.Resource(name = "persistencetemplate")
    protected RestTemplate restTemplate;
    @Autowired
    private IEstimatorSession session;

    @Override
    @WithPersistenceTryCatch
    public UserGroup saveUserGroup(UserGroup project) {
        ResponseEntity<UserGroup> response = restTemplate.exchange((String) session.getDataFromSession(GroupOperations.SAVE_USER_GROUP.getId()), HttpMethod.POST, new HttpEntity<UserGroup>(project, Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), UserGroup.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode()))
            LOG.debug("Create or update request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public UserGroup findUserGroup(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("groupId", id);
        ResponseEntity<UserGroup> response = restTemplate.exchange((String) session.getDataFromSession(GroupOperations.FIND_USER_GROUP.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), UserGroup.class, params);
        if (!HttpStatus.OK.equals(response.getStatusCode()))
            LOG.debug("Read by id request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public UserGroup findUserGroupByName(String name) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("name", name);
        ResponseEntity<UserGroup> response = restTemplate.exchange((String) session.getDataFromSession(GroupOperations.FIND_USER_GROUP_BY_NAME.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), UserGroup.class, params);
        if (!HttpStatus.OK.equals(response.getStatusCode()))
            LOG.debug("Read by name request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public List<UserGroup> findAllUserGroups() {
        ResponseEntity<UserGroup[]> response = restTemplate.exchange((String) session.getDataFromSession(GroupOperations.FIND_ALL_USES_GROUPS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), UserGroup[].class);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<UserGroup> findUserGroups(int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(3, 1);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        ResponseEntity<UserGroup[]> response = restTemplate.exchange((String) session.getDataFromSession(GroupOperations.PAGINATE_USER_GROUPS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), UserGroup[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteUserGroup(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("groupId", id);
        ResponseEntity<Void> response = restTemplate.exchange((String) session.getDataFromSession(GroupOperations.DELETE_USER_GROUP.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class, id);
        if (!HttpStatus.NO_CONTENT.equals(response.getStatusCode()))
            LOG.debug("Delete request did not succeed. Status code " + response.getStatusCode());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllUserGroups() {
        restTemplate.exchange((String) session.getDataFromSession(GroupOperations.DELETE_ALL_USER_GROUPS.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class);
    }

    @Override
    public Long countUserGroups() {
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(GroupOperations.COUNT_USES_GROUPS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class);
        return orderCounter.getBody();
    }


}
