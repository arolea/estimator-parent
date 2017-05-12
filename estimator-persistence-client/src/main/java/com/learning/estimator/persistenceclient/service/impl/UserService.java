package com.learning.estimator.persistenceclient.service.impl;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.common.session.IEstimatorSession;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.persistence.operations.UserOperations;
import com.learning.estimator.persistence.service.client.IUserServiceClientSide;
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
 * Consumes persistence operations for users
 *
 * @author rolea
 */
@Component
public class UserService implements IUserServiceClientSide {

    private static final ILogger LOG = LogManager.getLogger(UserService.class);
    @javax.annotation.Resource(name = "persistencetemplate")
    protected RestTemplate restTemplate;
    @Autowired
    private IEstimatorSession session;

    @Override
    @WithPersistenceTryCatch
    public User saveUser(User project) {
        ResponseEntity<User> response = restTemplate.exchange((String) session.getDataFromSession(UserOperations.SAVE_USER.getId()), HttpMethod.POST, new HttpEntity<User>(project, Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), User.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode()))
            LOG.debug("Create or update request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public User findUser(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", id);
        ResponseEntity<User> response = restTemplate.exchange((String) session.getDataFromSession(UserOperations.FIND_USER.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), User.class, params);
        if (!HttpStatus.OK.equals(response.getStatusCode()))
            LOG.debug("Read by id request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public User findUserByUsername(String name) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("name", name);
        ResponseEntity<User> response = restTemplate.exchange((String) session.getDataFromSession(UserOperations.FIND_USER_BY_NAME.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), User.class, params);
        if (!HttpStatus.OK.equals(response.getStatusCode()))
            LOG.debug("Read by name request did not succeed. Status code " + response.getStatusCode());
        return response.getBody();
    }

    @Override
    @WithPersistenceTryCatch
    public List<User> findAllUsers() {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", (Long) session.getDataFromSession("user_id"));
        ResponseEntity<User[]> response = restTemplate.exchange((String) session.getDataFromSession(UserOperations.FIND_ALL_USERS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), User[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public List<User> findUsers(int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(3, 1);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        ResponseEntity<User[]> response = restTemplate.exchange((String) session.getDataFromSession(UserOperations.PAGINATE_USERS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), User[].class, params);
        return Arrays.stream(response.getBody()).collect(Collectors.toList());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteUser(Long id) {
        Map<String, Object> params = new HashMap<>(2, 1);
        params.put("userId", id);
        ResponseEntity<Void> response = restTemplate.exchange((String) session.getDataFromSession(UserOperations.DELETE_USER.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class, params);
        if (!HttpStatus.NO_CONTENT.equals(response.getStatusCode()))
            LOG.debug("Delete request did not succeed. Status code " + response.getStatusCode());
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllUsers() {
        restTemplate.exchange((String) session.getDataFromSession(UserOperations.DELETE_ALL_USERS.getId()), HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class);
    }

    @Override
    @WithPersistenceTryCatch
    public Long countUsers() {
        ResponseEntity<Long> orderCounter = restTemplate.exchange((String) session.getDataFromSession(UserOperations.COUNT_USERS.getId()), HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Long.class);
        return orderCounter.getBody();
    }

}
