package com.learning.estimator.persistenceservice.tests;

import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.model.infrastructure.LoginOutcome;
import com.learning.estimator.persistenceservice.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * HATEOAS tests ( link discovery tests )
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class HateoasTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${api.persistence.task.entrypoint}")
    private String taskEntrypoint;
    @Value("${api.persistence.project.entrypoint}")
    private String projectEntrypoint;
    @Value("${api.persistence.user.entrypoint}")
    private String userEntrypoint;
    @Value("${api.persistence.group.entrypoint}")
    private String groupEntrypoint;
    @Value("${api.persistence.version}")
    private String apiVersion;

    private String taskUrl;
    private String projectUrl;
    private String userUrl;
    private String groupUrl;

    @Before
    public void init() {
        taskUrl = "/" + apiVersion + "/" + taskEntrypoint;
        projectUrl = "/" + apiVersion + "/" + projectEntrypoint;
        userUrl = "/" + apiVersion + "/" + userEntrypoint;
        groupUrl = "/" + apiVersion + "/" + groupEntrypoint;
    }

    @Test
    public void testHateoas() {
        //test entrypoint
        System.out.println("Admin login : ");
        ResponseEntity<Resource<LoginOutcome>> response;
        response = restTemplate.exchange("/" + apiVersion + "/login", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), new ParameterizedTypeReference<Resource<LoginOutcome>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody().getContent());
        response.getBody().getLinks().forEach(System.out::println);
        System.out.println();

        testDiscovery(groupUrl, UserRole.ROLE_ADMIN, "groups controller");
        testDiscovery(groupUrl, UserRole.ROLE_USER, "groups controller");

        testDiscovery(userUrl, UserRole.ROLE_ADMIN, "users controller");
        testDiscovery(userUrl, UserRole.ROLE_USER, "users controller");

        testDiscovery(projectUrl, UserRole.ROLE_ADMIN, "projects controller");
        testDiscovery(projectUrl, UserRole.ROLE_USER, "projects controller");

        testDiscovery(taskUrl, UserRole.ROLE_ADMIN, "tasks controller");
        testDiscovery(taskUrl, UserRole.ROLE_USER, "tasks controller");
    }

    public void testDiscovery(String url, UserRole role, String controllerName) {
        System.out.println("Testing " + controllerName + " discovery for user role " + role);
        ResponseEntity<Resource<String>> response;
        response = restTemplate.exchange(url + "/discover/?userRole={userRole}", HttpMethod.OPTIONS, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), new ParameterizedTypeReference<Resource<String>>() {
        }, role);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        response.getBody().getLinks().forEach(System.out::println);
        System.out.println();
    }

}
