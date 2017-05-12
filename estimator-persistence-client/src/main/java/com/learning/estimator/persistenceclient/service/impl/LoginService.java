package com.learning.estimator.persistenceclient.service.impl;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.common.session.IEstimatorSession;
import com.learning.estimator.model.infrastructure.LoginOutcome;
import com.learning.estimator.persistenceclient.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Consumes login / logout operations
 *
 * @author rolea
 */
@Component
public class LoginService {

    private static final ILogger LOG = LogManager.getLogger(LoginService.class);
    @javax.annotation.Resource(name = "persistencetemplate")
    private RestTemplate restTemplate;
    @Autowired
    private IEstimatorSession session;
    @Value("${api.persistence.version}")
    private String apiVersion;
    @Value("${api.persistence.name}")
    private String apiName;

    private String url;

    @PostConstruct
    public void init() {
        url = apiName + "/" + apiVersion;
        LOG.info("Login service url : " + url);
    }

    @WithPersistenceTryCatch
    public LoginOutcome login(String currentUsername, String currentPassword) {
        ResponseEntity<Resource<LoginOutcome>> response = restTemplate.exchange(url + "/login", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate(currentUsername, currentPassword)), new ParameterizedTypeReference<Resource<LoginOutcome>>() {
        });
        response.getBody().getLinks().forEach(link -> {
            ResponseEntity<Resource<String>> currentController = restTemplate.exchange(link.getHref(), HttpMethod.OPTIONS, new HttpEntity<Void>(Utils.authenticate(currentUsername, currentPassword)), new ParameterizedTypeReference<Resource<String>>() {
            });
            currentController.getBody().getLinks().forEach(innerLink -> {
                session.storeDataIntoSession(innerLink.getRel(), innerLink.getHref());
            });
        });
        return response.getBody().getContent();
    }

    @WithPersistenceTryCatch
    public void logout(Long userId) {
        restTemplate.exchange(url + "/logout", HttpMethod.POST, new HttpEntity<>(userId, Utils.authenticate((String) session.getDataFromSession("username"), (String) session.getDataFromSession("password"))), Void.class);
    }

}
